const API_URL = import.meta.env.VITE_API_URL || "http://localhost:8080";

export function getToken() {
  return localStorage.getItem("marcuss-burgers:token") || "";
}

export function setToken(token) {
  localStorage.setItem("marcuss-burgers:token", token);
  window.dispatchEvent(new Event("auth-change"));
}

export function clearToken() {
  localStorage.removeItem("marcuss-burgers:token");
  window.dispatchEvent(new Event("auth-change"));
}

export function getUserProfile() {
  const token = getToken();
  if (!token) return null;

  try {
    const payload = token.split(".")[1]
      .replace(/-/g, "+")
      .replace(/_/g, "/");
    const decoded = JSON.parse(atob(payload));
    return {
      name: decoded.name || decoded.sub || "Usuário",
      email: decoded.sub || "",
      role: decoded.role || "",
    };
  } catch {
    return null;
  }
}

async function request(path, options = {}) {
  const token = getToken();
  const headers = {
    "Content-Type": "application/json",
    ...(options.headers || {}),
  };
  if (token) headers.Authorization = `Bearer ${token}`;

  const response = await fetch(`${API_URL}${path}`, { ...options, headers });
  if (!response.ok) {
    let message = "Não foi possível concluir a operação.";
    try {
      const data = await response.json();
      message = data.message || data.error || message;
    } catch {
      // Mantém a mensagem padrão.
    }
    throw new Error(message);
  }

  if (response.status === 204) return null;
  return response.json();
}

export const api = {
  products: () => request("/products"),
  login: (email, password) => request("/auth/login", {
    method: "POST",
    body: JSON.stringify({ email, password }),
  }),
  register: (name, email, password) => request("/auth/register", {
    method: "POST",
    body: JSON.stringify({ name, email, password }),
  }),
  addresses: () => request("/addresses"),
  findAddressByZipCode: (zipCode) => request(`/addresses/cep/${zipCode.replace(/\D/g, "")}`),
  createAddress: (address) => request("/addresses", {
    method: "POST",
    body: JSON.stringify(address),
  }),
  orders: () => request("/orders"),
  createOrder: (addressId, items) => request("/orders", {
    method: "POST",
    body: JSON.stringify({
      addressId,
      items: items.map(({ product, quantity }) => ({
        productId: product.productId || product.id,
        quantity,
      })),
    }),
  }),
};
