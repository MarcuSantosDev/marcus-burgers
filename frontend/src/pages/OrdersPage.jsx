import { useEffect, useRef, useState } from "react";
import { Link, useParams } from "react-router-dom";
import { api } from "../services/api";
import { formatCurrency } from "../utils/formatCurrency";
import "./Pages.css";

const statusLabels = {
  PENDING: "Pendente",
  PREPARING: "Preparando",
  READY: "Pronto - pedido a caminho",
  DELIVERED: "Entregue",
  CANCELLED: "Cancelado",
};

const isClosedStatus = (status) => status === "DELIVERED" || status === "CANCELLED";
const progressStatuses = ["PENDING", "PREPARING", "READY", "DELIVERED"];

export default function OrdersPage() {
  const { orderId } = useParams();
  const [orders, setOrders] = useState([]);
  const [error, setError] = useState("");
  const previousStatuses = useRef(new Map());

  useEffect(() => {
    let cancelled = false;

    async function loadOrders() {
      try {
        const items = await api.orders();
        if (cancelled) return;

        items.forEach((order) => {
          const previous = previousStatuses.current.get(order.orderId);
          if (previous && previous !== "DELIVERED" && order.status === "DELIVERED") {
            playCelebration();
          }
          previousStatuses.current.set(order.orderId, order.status);
        });
        setOrders(items);
        setError("");
      } catch (requestError) {
        if (!cancelled) setError(requestError.message);
      }
    }

    loadOrders();
    const intervalId = window.setInterval(loadOrders, 4000);
    return () => {
      cancelled = true;
      window.clearInterval(intervalId);
    };
  }, []);

  const visibleOrders = orderId ? orders.filter((order) => String(order.orderId) === orderId) : orders;

  return (
    <main className="page">
      <p className="eyebrow">ACOMPANHAMENTO</p>
      <h1 className="page__title">{orderId ? "Pedido confirmado" : "Meus pedidos"}</h1>
      {orderId && <p className="success-message">Seu pedido foi recebido e já está na cozinha.</p>}
      {error && <p className="form__error" role="alert">{error}</p>}
      {(() => {
        const activeOrders = visibleOrders.filter((order) => !isClosedStatus(order.status));
        const closedOrders = visibleOrders.filter((order) => isClosedStatus(order.status));

        const renderOrder = (order) => {
          const closed = isClosedStatus(order.status);
          return (
            <article className={`order-card ${closed ? "order-card--closed" : "order-card--active"} ${order.status === "CANCELLED" ? "order-card--cancelled" : ""}`} key={order.orderId}>
              <header><div><span>Pedido #{order.orderId}</span><strong>{statusLabels[order.status] || order.status}</strong></div><time>{order.orderDate ? new Date(order.orderDate).toLocaleString("pt-BR") : ""}</time></header>
              <div
                className={`delivery-progress delivery-progress--${order.status.toLowerCase()}`}
                style={{
                  "--progress-width": `${progressStatuses.indexOf(order.status) >= 0 ? progressStatuses.indexOf(order.status) * 33.333 : 0}%`,
                  "--motor-left": `${progressStatuses.indexOf(order.status) >= 0 ? 12.5 + progressStatuses.indexOf(order.status) * 25 : 12.5}%`,
                }}
                aria-label={`Status do pedido: ${statusLabels[order.status]}`}
              >
                <div className="delivery-line"><span /></div>
                <div className="delivery-steps">
                  {progressStatuses.map((status, index) => (
                    <div className="delivery-step" key={status}>
                      <span className={`delivery-dot ${index <= progressStatuses.indexOf(order.status) ? "delivery-dot--active" : ""}`} />
                      <small>{statusLabels[status]}</small>
                    </div>
                  ))}
                </div>
                {order.status !== "CANCELLED" && <span className="delivery-motorbike" aria-hidden="true">🛵</span>}
                {order.status === "CANCELLED" && <span className="delivery-cancelled" aria-hidden="true">✕</span>}
                <span className="delivery-house" aria-hidden="true">🏠</span>
              </div>
              {closed && <div className="closed-order-label">Pedido encerrado · {statusLabels[order.status]}</div>}
              <ul>{order.items?.map((item) => <li key={item.orderItemId}><span>{item.quantity}× {item.productName}</span><strong>{formatCurrency(item.subtotal)}</strong></li>)}</ul>
              <footer><span>Total</span><strong>{formatCurrency(order.totalAmount)}</strong></footer>
            </article>
          );
        };

        return (
          <>
            {activeOrders.length > 0 && (
              <section className="orders-section">
                <h2>Pedidos em andamento</h2>
                <div className="orders-list">{activeOrders.map(renderOrder)}</div>
              </section>
            )}
            {closedOrders.length > 0 && (
              <section className="orders-section orders-section--closed">
                <h2>Pedidos encerrados</h2>
                <div className="orders-list">{closedOrders.map(renderOrder)}</div>
              </section>
            )}
            {!error && visibleOrders.length === 0 && <p className="empty-state">Nenhum pedido encontrado.</p>}
          </>
        );
      })()}
      <Link className="secondary-button" to="/">Voltar ao cardápio</Link>
    </main>
  );
}

function playCelebration() {
  try {
    const AudioContext = window.AudioContext || window.webkitAudioContext;
    if (!AudioContext) return;

    const context = new AudioContext();
    const notes = [523.25, 659.25, 783.99, 1046.5];
    notes.forEach((frequency, index) => {
      const oscillator = context.createOscillator();
      const gain = context.createGain();
      const start = context.currentTime + index * 0.12;
      oscillator.type = "sine";
      oscillator.frequency.value = frequency;
      gain.gain.setValueAtTime(0.0001, start);
      gain.gain.exponentialRampToValueAtTime(0.18, start + 0.02);
      gain.gain.exponentialRampToValueAtTime(0.0001, start + 0.25);
      oscillator.connect(gain);
      gain.connect(context.destination);
      oscillator.start(start);
      oscillator.stop(start + 0.26);
    });
  } catch {
    // Navegadores podem bloquear áudio; o fluxo visual continua normalmente.
  }
}
