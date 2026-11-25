import { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { api, getToken, getUserProfile } from "../services/api";
import { formatCurrency } from "../utils/formatCurrency";
import "./Pages.css";

const emptyAddress = {
  name: "", street: "", number: "", complement: "", neighborhood: "", city: "", state: "", zipCode: "",
};

export default function CheckoutPage({ cart, onSuccess }) {
  const navigate = useNavigate();
  const [addresses, setAddresses] = useState([]);
  const [addressId, setAddressId] = useState("");
  const [address, setAddress] = useState(emptyAddress);
  const [showAddress, setShowAddress] = useState(false);
  const [error, setError] = useState(() => getToken() ? "" : "Faça login para finalizar o pedido.");
  const [loading, setLoading] = useState(false);
  const [zipCodeLoading, setZipCodeLoading] = useState(false);

  useEffect(() => {
    if (!getToken()) return;
    api.addresses().then((items) => {
      setAddresses(items);
      if (items.length) setAddressId(String(items[0].addressId));
      else setError("Cadastre um endereço para finalizar o pedido.");
    }).catch((requestError) => setError(requestError.message));
  }, []);

  async function lookupZipCode(rawZipCode) {
    const zipCode = rawZipCode.replace(/\D/g, "");
    if (zipCode.length !== 8) return;

    setZipCodeLoading(true);
    setError("");
    setAddress((current) => ({
      ...current,
      street: "",
      complement: "",
      neighborhood: "",
      city: "",
      state: "",
    }));
    try {
      const result = await api.findAddressByZipCode(zipCode);
      if (result.error) {
        setError("CEP não encontrado.");
        return;
      }
      setAddress((current) => ({
        ...current,
        street: result.street || result.logradouro || "",
        complement: result.complement || result.complemento || current.complement,
        neighborhood: result.neighborhood || result.bairro || "",
        city: result.city || result.localidade || "",
        state: result.state || result.uf || "",
        zipCode: result.zipCode || result.cep || rawZipCode,
      }));
    } catch (requestError) {
      setError(requestError.message);
    } finally {
      setZipCodeLoading(false);
    }
  }

  function update(event) {
    const { name, value } = event.target;

    if (name === "zipCode") {
      const digits = value.replace(/\D/g, "").slice(0, 8);
      const formatted = digits.length > 5
        ? `${digits.slice(0, 5)}-${digits.slice(5)}`
        : digits;
      setAddress((current) => ({ ...current, zipCode: formatted }));
      if (digits.length === 8) lookupZipCode(digits);
      return;
    }

    setAddress((current) => ({ ...current, [name]: value }));
  }

  async function saveAddress(event) {
    event.preventDefault();
    setLoading(true);
    setError("");
    try {
      const created = await api.createAddress(address);
      setAddresses((current) => [...current, created]);
      setAddressId(String(created.addressId));
      setShowAddress(false);
      setAddress(emptyAddress);
      setError("");
    } catch (requestError) {
      setError(requestError.message);
    } finally {
      setLoading(false);
    }
  }

  async function submitOrder(event) {
    event.preventDefault();
    if (cart.items.length === 0) {
      setError("");
      return;
    }
    if (!getToken()) {
      setError("Faça login para finalizar o pedido.");
      return;
    }
    if (!addressId) {
      setError("Cadastre um endereço para finalizar o pedido.");
      return;
    }
    if (getUserProfile()?.role === "ADMIN") {
      setError("Administradores não podem fazer pedidos.");
      return;
    }
    setLoading(true);
    setError("");
    try {
      const order = await api.createOrder(Number(addressId), cart.items);
      await onSuccess?.();
      navigate(`/pedidos/${order.orderId}`);
    } catch (requestError) {
      setError(requestError.message);
    } finally {
      setLoading(false);
    }
  }

  return (
    <main className="page">
      <p className="eyebrow">FINALIZAR COMPRA</p>
      <h1 className="page__title">Seu pedido</h1>
      <div className="checkout-grid">
        <section className="panel">
          <h2>Entrega</h2>
          {addresses.length > 0 && (
            <label className="form__label">Endereço
              <select value={addressId} onChange={(event) => setAddressId(event.target.value)}>
                {addresses.map((item) => <option key={item.addressId} value={item.addressId}>{item.name} — {item.street}, {item.number}</option>)}
              </select>
            </label>
          )}
          {!getToken() && <p className="form__error" role="alert">Faça login para finalizar o pedido.</p>}
          {!getToken() && <Link className="primary-button" to="/login">Entrar ou criar conta</Link>}
          {getToken() && addresses.length === 0 && <p className="form__error" role="alert">Cadastre um endereço antes de finalizar o pedido.</p>}
          <button className="secondary-button" type="button" onClick={() => setShowAddress((value) => !value)}>{showAddress ? "Cancelar" : "Cadastrar novo endereço"}</button>
          {showAddress && (
            <form className="form address-form" onSubmit={saveAddress}>
              <label>Nome<input name="name" value={address.name} onChange={update} required /></label>
              <label>Rua<input name="street" value={address.street} onChange={update} required /></label>
              <div className="form-row"><label>Número<input name="number" value={address.number} onChange={update} required /></label><label>CEP<input name="zipCode" value={address.zipCode} onChange={update} onBlur={() => { const digits = address.zipCode.replace(/\D/g, ""); if (digits.length === 8) lookupZipCode(digits); }} placeholder="58010-000" inputMode="numeric" autoComplete="postal-code" aria-busy={zipCodeLoading} required /><span className="zip-code-status" aria-live="polite">{zipCodeLoading ? "Consultando CEP..." : ""}</span></label></div>
              <label>Complemento<input name="complement" value={address.complement} onChange={update} /></label>
              <label>Bairro<input name="neighborhood" value={address.neighborhood} onChange={update} required /></label>
              <div className="form-row"><label>Cidade<input name="city" value={address.city} onChange={update} required /></label><label>Estado<input name="state" value={address.state} onChange={update} required /></label></div>
              <button className="primary-button" type="submit" disabled={loading}>Salvar endereço</button>
            </form>
          )}
        </section>
        <aside className="panel order-summary">
          <h2>Resumo</h2>
          {cart.items.map(({ product, quantity }) => <div className="summary-line" key={product.id}><span>{quantity}× {product.name}</span><strong>{formatCurrency(product.price * quantity)}</strong></div>)}
          <div className="summary-total"><span>Total</span><strong>{formatCurrency(cart.totalPrice)}</strong></div>
          {error && <p className="form__error" role="alert">{error}</p>}
          {getUserProfile()?.role !== "ADMIN" && (
            <button className="primary-button" type="button" onClick={submitOrder} disabled={loading || cart.items.length === 0}>{loading ? "Finalizando..." : "Confirmar pedido"}</button>
          )}
          {!getToken() && (
            <p>
              Para finalizar a compra, é necessário <Link to="/login" className="link">fazer login</Link> na sua conta.
            </p>
          )}
        </aside>
      </div>
    </main>
  );
}
