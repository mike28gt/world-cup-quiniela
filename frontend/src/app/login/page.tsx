"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";
import { loginUser } from "@/features/auth/authApi";
import { saveAccessToken } from "@/features/auth/tokenStorage";

export default function LoginPage() {
  const router = useRouter();

  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  const [errorMessage, setErrorMessage] = useState("");
  const [isSubmitting, setIsSubmitting] = useState(false);

  async function handleSubmit(
    event: React.FormEvent<HTMLFormElement>
  ) {
    event.preventDefault();

    setErrorMessage("");
    setIsSubmitting(true);

    try {
      const response = await loginUser({
        email,
        password,
      });

      localStorage.setItem("accessToken", response.accessToken);
      saveAccessToken(response.accessToken);

      router.push("/dashboard");
    } catch (error) {
      console.error(error);

      setErrorMessage(
        "Invalid email or password."
      );
    } finally {
      setIsSubmitting(false);
    }
  }

  return (
    <main className="min-h-screen flex items-center justify-center p-6">
      <div className="w-full max-w-md border rounded-lg p-6 shadow">
        <h1 className="text-2xl font-bold mb-6">
          Sign In
        </h1>

        {errorMessage && (
          <div className="mb-4 border border-red-300 bg-red-50 p-3 rounded text-red-700 text-sm">
            {errorMessage}
          </div>
        )}

        <form
          className="space-y-4"
          onSubmit={handleSubmit}
        >
          <div>
            <label className="block mb-1">
              Email
            </label>

            <input
              type="email"
              value={email}
              onChange={(event) =>
                setEmail(event.target.value)
              }
              className="w-full border rounded px-3 py-2"
              required
            />
          </div>

          <div>
            <label className="block mb-1">
              Password
            </label>

            <input
              type="password"
              value={password}
              onChange={(event) =>
                setPassword(event.target.value)
              }
              className="w-full border rounded px-3 py-2"
              required
            />
          </div>

          <button
            type="submit"
            disabled={isSubmitting}
            className="w-full bg-black text-white rounded py-2 disabled:opacity-60"
          >
            {isSubmitting ? "Signing in..." : "Sign In"}
          </button>
        </form>
      </div>
    </main>
  );
}