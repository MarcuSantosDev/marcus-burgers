import { useEffect, useState } from "react";
import { Navigate } from "react-router-dom";
import { api, getUserProfile } from "../services/api";
import { formatCurrency } from "../utils/formatCurrency";
import "./Pages.css";

const statusLabels = {
  PENDING: "Pendente",
  PREPARING: "Preparando",
  READY: "Pronto",
  DELIVERED: "Entregue",
  CANCELLED: "Cancelado",
};

const nextStatuses = {
  PENDING: ["PREPARING", "CANCELLED"],
  PREPARING: ["READY", "CANCELLED"],
  READY: ["DELIVERED", "CANCELLED"],
  DELIVERED: [],
  CANCELLED: [],
};

export default function AdminOrdersPage() {
  const profile = getUserProfile();
  const [orders, setOrders] = useState([]);
  const [error, setError] = useState("");
  const [updating, setUpdating] = useState(null);

  function loadOrders() {
    api.orders()
      .then(setOrders)
      .catch((requestError) => setError(requestError.message));
  }

  useEffect(loadOrders, []);

  async function changeStatus(orderId, status) {
    setUpdating(orderId);
    setError("");
    try {
      const updated = await api.updateOrderStatus(orderId, status);
      setOrders((current) => current.map((order) =>
        order.orderId === updated.orderId ? updated : order
      ));
    } catch (requestError) {
      setError(requestError.message);
    } finally {
      setUpdating(null);
    }
  }

  if (profile?.role !== "ADMIN") return <Navigate to="/" replace />;

  return (
    <main className="page">
      <p className="eyebrow">ÁREA ADMINISTRATIVA</p>
      <h1 className="page__title">Controle de pedidos</h1>
      {error && <p className="form__error" role="alert">{error}</p>}

      <div className="admin-orders">
        {orders.map((order) => (
          <article className={`order-card admin-order-card ${order.status === "CANCELLED" ? "order-card--cancelled" : ""}`} key={order.orderId}>
            <header>
              <div>
                <span>Pedido #{order.orderId}</span>
                <strong>{statusLabels[order.status] || order.status}</strong>
              </div>
              <time>{order.orderDate ? new Date(order.orderDate).toLocaleString("pt-BR") : ""}</time>
            </header>

            <ul>
              {order.items?.map((item) => (
                <li key={item.orderItemId}>
                  <span>{item.quantity}× {item.productName}</span>
                  <strong>{formatCurrency(item.subtotal)}</strong>
                </li>
              ))}
            </ul>

            <footer>
              <span>Total: <strong>{formatCurrency(order.totalAmount)}</strong></span>
              <label className="admin-status-control">
                Alterar status
                <select
                  value={order.status}
                  disabled={updating === order.orderId || nextStatuses[order.status]?.length === 0}
                  onChange={(event) => changeStatus(order.orderId, event.target.value)}
                >
                  <option value={order.status}>{statusLabels[order.status]}</option>
                  {nextStatuses[order.status]?.map((status) => (
                    <option key={status} value={status}>{statusLabels[status]}</option>
                  ))}
                </select>
              </label>
            </footer>
          </article>
        ))}
        {orders.length === 0 && !error && <p className="empty-state">Nenhum pedido encontrado.</p>}
      </div>
    </main>
  );
}
