"use client";

import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import { getCurrentUser, MeResponse } from "@/features/auth/authApi";
import { getAccessToken } from "@/features/auth/tokenStorage";

export default function DashboardPage() {

  const router = useRouter();

  const [user, setUser] =
    useState<MeResponse | null>(null);

  const [isLoading, setIsLoading] =
    useState(true);

  useEffect(() => {

    async function loadUser() {

      const token = getAccessToken();

      if (!token) {
        router.push("/login");
        return;
      }

      try {

        const currentUser = await getCurrentUser();

        setUser(currentUser);

      } catch {

        router.push("/login");

      } finally {

        setIsLoading(false);
      }
    }

    loadUser();

  }, [router]);

  if (isLoading) {
    return (
      <main className="p-6">
        Loading dashboard...
      </main>
    );
  }

  return (
    <main className="p-6">

      <h1 className="text-3xl font-bold">
        Dashboard
      </h1>

      <p className="mt-4">
        Authenticated successfully.
      </p>

      <div className="mt-6 border rounded p-4">

        <p>
          <strong>User ID:</strong>{" "}
          {user?.userId}
        </p>

        <p className="mt-2">
          <strong>Email:</strong>{" "}
          {user?.email}
        </p>

      </div>
    </main>
  );
}