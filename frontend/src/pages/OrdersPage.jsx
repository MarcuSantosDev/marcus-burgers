import { useEffect, useState } from "react";
import { Link, useParams } from "react-router-dom";
import { api } from "../services/api";
import { formatCurrency } from "../utils/formatCurrency";
import "./Pages.css";

const statusLabels = { PENDING: "Pendente", PAID: "Pago", SHIPPED: "Em entrega", DELIVERED: "Entregue", CANCELED: "Cancelado" };

export default function OrdersPage() {
  const { orderId } = useParams();
  const [orders, setOrders] = useState([]);
  const [error, setError] = useState("");

  useEffect(() => {
    api.orders().then(setOrders).catch((requestError) => setError(requestError.message));
  }, []);

  const visibleOrders = orderId ? orders.filter((order) => String(order.orderId) === orderId) : orders;

  return (
    <main className="page">
      <p className="eyebrow">ACOMPANHAMENTO</p>
      <h1 className="page__title">{orderId ? "Pedido confirmado" : "Meus pedidos"}</h1>
      {orderId && <p className="success-message">Seu pedido foi recebido e já está na cozinha.</p>}
      {error && <p className="form__error" role="alert">{error}</p>}
      <div className="orders-list">
        {visibleOrders.map((order) => (
          <article className="order-card" key={order.orderId}>
            <header><div><span>Pedido #{order.orderId}</span><strong>{statusLabels[order.status] || order.status}</strong></div><time>{order.orderDate ? new Date(order.orderDate).toLocaleString("pt-BR") : ""}</time></header>
            <ul>{order.items?.map((item) => <li key={item.orderItemId}><span>{item.quantity}× {item.productName}</span><strong>{formatCurrency(item.subtotal)}</strong></li>)}</ul>
            <footer><span>Total</span><strong>{formatCurrency(order.totalAmount)}</strong></footer>
          </article>
        ))}
        {!error && visibleOrders.length === 0 && <p className="empty-state">Nenhum pedido encontrado.</p>}
      </div>
      <Link className="secondary-button" to="/">Voltar ao cardápio</Link>
    </main>
  );
}
