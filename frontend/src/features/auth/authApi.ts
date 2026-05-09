import { apiFetch } from "@/lib/api";
import { AuthResponse, RegisterRequest } from "./types";

export async function registerUser(
    request: RegisterRequest
): Promise<AuthResponse> {
    return apiFetch<AuthResponse>("/auth/register", {
    method: "POST",
    body: JSON.stringify(request),
    });
}

export async function loginUser(
    request: LoginRequest
): Promise<AuthResponse> {
    return apiFetch<AuthResponse>("/auth/login", {
        method: "POST",
        body: JSON.stringify(request),
        });
}