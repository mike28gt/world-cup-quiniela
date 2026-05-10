import { apiFetch } from "@/lib/api";
import { AuthResponse, RegisterRequest } from "./types";
import { getAccessToken } from "./tokenStorage"

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

export type MeResponse = {
    userId: string;
    email: string;
}

export async function getCurrentUser(): Promise<MeResponse> {
    const token = getAccessToken();

    return apiFetch<MeResponse>("/me", {
        method: "GET",
        headers: {
            Authorization: `Bearer ${token}`,
            },
        });
}