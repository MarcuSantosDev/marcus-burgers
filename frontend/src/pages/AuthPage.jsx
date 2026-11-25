import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { api, getUserProfile, setToken } from "../services/api";
import "./Pages.css";

export default function AuthPage({ mode = "login" }) {
  const navigate = useNavigate();
  const isRegister = mode === "register";
  const [form, setForm] = useState({ name: "", email: "", password: "" });
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  function update(event) {
    setForm((current) => ({ ...current, [event.target.name]: event.target.value }));
  }

  async function submit(event) {
    event.preventDefault();
    setError("");

    if (isRegister && form.password.length < 8) {
      setError("A senha deve ter pelo menos 8 caracteres.");
      return;
    }

    setLoading(true);
    try {
      if (isRegister) {
        await api.register(form.name, form.email, form.password);
        const { token } = await api.login(form.email, form.password);
        setToken(token);
      } else {
        const { token } = await api.login(form.email, form.password);
        setToken(token);
      }
      const profile = getUserProfile();
      navigate(profile?.role === "ADMIN" ? "/admin/pedidos" : "/checkout");
    } catch (requestError) {
      setError(requestError.message);
    } finally {
      setLoading(false);
    }
  }

  return (
    <main className="page page--narrow">
      <section className="auth-card">
        <p className="eyebrow">{isRegister ? "CRIAR CONTA" : "BEM-VINDO"}</p>
        <h1>{isRegister ? "Cadastre-se" : "Entrar"}</h1>
        <form className="form" onSubmit={submit}>
          {isRegister && (
            <label>Nome<input name="name" value={form.name} onChange={update} required /></label>
          )}
          <label>E-mail<input name="email" type="email" value={form.email} onChange={update} required /></label>
          <label>
            Senha
            <input
              name="password"
              type="password"
              minLength={8}
              maxLength={72}
              value={form.password}
              onChange={update}
              required
              aria-describedby="password-hint"
            />
            <small id="password-hint" className="form__hint">Use no mínimo 8 caracteres.</small>
          </label>
          {error && <p className="form__error" role="alert">{error}</p>}
          <button className="primary-button" type="submit" disabled={loading}>{loading ? "Aguarde..." : isRegister ? "Criar conta" : "Entrar"}</button>
        </form>
        <p className="auth-card__switch">
          {isRegister ? "Já tem conta?" : "Ainda não tem conta?"}{" "}
          <Link to={isRegister ? "/login" : "/register"}>{isRegister ? "Entrar" : "Criar conta"}</Link>
        </p>
      </section>
    </main>
  );
}
