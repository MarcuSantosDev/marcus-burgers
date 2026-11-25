import { useEffect, useState } from "react";
import { BrowserRouter, Route, Routes, useLocation, Navigate } from "react-router-dom";
import { useCart } from "./hooks/useCart";
import { api, clearToken, getToken, getUserProfile } from "./services/api";
import Header from "./components/Header/Header";
import Hero from "./components/Hero/Hero";
import Menu from "./components/Menu/Menu";
import About from "./components/About/About";
import Footer from "./components/Footer/Footer";
import CartDrawer from "./components/CartDrawer/CartDrawer";
import AuthPage from "./pages/AuthPage";
import CheckoutPage from "./pages/CheckoutPage";
import OrdersPage from "./pages/OrdersPage";
import AdminOrdersPage from "./pages/AdminOrdersPage";
import { AdminCategoriesPage, AdminProductsPage } from "./pages/AdminCatalogPage";
import "./App.css";

const fallbackProducts = [
	{
		id: 1,
		name: "Classic Burger",
		description:
			"Pão brioche, hambúrguer artesanal, queijo cheddar e molho especial.",
		price: 29.9,
		stock: 50,
		imageId: "photo-1568901346375-23c9450c58cd",
	},
	{
		id: 2,
		name: "Double Cheese",
		description:
			"Dois hambúrgueres, cheddar duplo, cebola caramelizada e brioche.",
		price: 36.9,
		stock: 50,
		imageId: "photo-1571091718767-18b5b1457add",
	},
	{
		id: 3,
		name: "Bacon Supreme",
		description:
			"Hambúrguer artesanal, bacon crocante, queijo e molho barbecue.",
		price: 34.9,
		stock: 50,
		imageId: "photo-1550547660-d9450f859349",
	},
];

function StorePage({ products, onAddToCart, isAdmin }) {
	return (
		<>
			<main id="conteudo">
				<Hero />
				<Menu products={products} onAddToCart={onAddToCart} isAdmin={isAdmin} />
				<About />
			</main>
			<Footer />
		</>
	);
}

function HashScroll() {
	const { pathname, hash } = useLocation();

	useEffect(() => {
		const targetId = hash.replace("#", "");
		if (!targetId) return;

		requestAnimationFrame(() => {
			const element = document.getElementById(targetId);
			if (element) element.scrollIntoView({ behavior: "smooth", block: "start" });
		});
	}, [pathname, hash]);

	return null;
}

function AppRoutes() {
	const [isCartOpen, setIsCartOpen] = useState(false);
	const [products, setProducts] = useState(fallbackProducts);
	const [authenticated, setAuthenticated] = useState(Boolean(getToken()));
	const profile = authenticated ? getUserProfile() : null;
	const isAdmin = profile?.role === "ADMIN";
	const [cartFlight, setCartFlight] = useState(null);
	const cart = useCart(products);

	useEffect(() => {
		const onAuthChange = () => setAuthenticated(Boolean(getToken()));
		window.addEventListener("auth-change", onAuthChange);

		loadProducts();

		return () => window.removeEventListener("auth-change", onAuthChange);
	}, []);

	function loadProducts() {
		return api.products()
			.then((items) => {
				setProducts(
					items.map((product) => ({
						...product,
						id: product.productId,
						price: Number(product.price),
						imageUrl: product.imageUrl,
						imageId: "photo-1568901346375-23c9450c58cd",
					}))
				);
			})
			.catch(() => setProducts(fallbackProducts));
	}

	function logout() {
		clearToken();
		setAuthenticated(false);
	}

	function addToCart(product, button) {
		if (isAdmin) return; // Impede administradores de adicionar produtos ao carrinho

		const source =
			button?.getBoundingRect?.() || button?.getBoundingClientRect?.();
		const target = document.querySelector(".header__cart")?.getBoundingClientRect();

		if (source && target) {
			setCartFlight({
				id: `${product.id}-${Date.now()}`,
				imageUrl: product.imageUrl,
				imageId: product.imageId,
				startX: source.left + source.width / 2,
				startY: source.top + source.height / 2,
				endX: target.left + target.width / 2,
				endY: target.top + target.height / 2,
				deltaX:
					target.left + target.width / 2 - (source.left + source.width / 2),
				deltaY:
					target.top + target.height / 2 - (source.top + source.height / 2),
				halfDeltaX:
					(target.left + target.width / 2 - (source.left + source.width / 2)) *
					0.5,
				arcDeltaY:
					(target.top + target.height / 2 - (source.top + source.height / 2)) *
						0.25 -
					80,
				midX:
					((source.left + source.width / 2) + (target.left + target.width / 2)) /
					2,
				arcY:
					Math.min(source.top + source.height / 2, target.top + target.height / 2) -
					80,
			});
		}

		cart.add(product.id, Number(product.stock) || 0);
	}

	return (
		<>
			<Header
				cartCount={cart.totalItems}
				onOpenCart={() => setIsCartOpen(true)}
				authenticated={authenticated}
				onLogout={logout}
				isAdmin={isAdmin}
			/>

			<Routes>
				<Route
					path="/"
					element={<StorePage products={products} onAddToCart={addToCart} isAdmin={isAdmin} />}
				/>
				<Route path="/login" element={<AuthPage mode="login" />} />
				<Route path="/register" element={<AuthPage mode="register" />} />
				<Route
					path="/checkout"
					element={
						authenticated ? (
							<CheckoutPage
								cart={cart}
								onSuccess={async () => {
									cart.clear();
									await loadProducts();
								}}
							/>
						) : (
							<Navigate to="/login" replace />
						)
					}
				/>
				<Route
					path="/pedidos"
					element={isAdmin ? <Navigate to="/admin/pedidos" replace /> : <OrdersPage />}
				/>
				<Route
					path="/pedidos/:orderId"
					element={isAdmin ? <Navigate to="/admin/pedidos" replace /> : <OrdersPage />}
				/>
				<Route
					path="/admin/categorias"
					element={<AdminCategoriesPage />}
				/>
				<Route
					path="/admin/produtos"
					element={<AdminProductsPage />}
				/>
				<Route path="/admin/pedidos" element={<AdminOrdersPage />} />
				<Route path="/admin/pedidos/:orderId" element={<AdminOrdersPage />} />
			</Routes>

			<CartDrawer
				open={isCartOpen}
				onClose={() => setIsCartOpen(false)}
				items={cart.items}
				totalItems={cart.totalItems}
				totalPrice={cart.totalPrice}
				onIncrement={cart.increment}
				onDecrement={cart.decrement}
				onRemove={cart.remove}
				onClear={cart.clear}
				checkoutHref="/checkout"
			/>

			{cartFlight && (
				<div
					key={cartFlight.id}
					className="cart-flight"
					style={{
						"--flight-start-x": `${cartFlight.startX}px`,
						"--flight-start-y": `${cartFlight.startY}px`,
						"--flight-end-x": `${cartFlight.endX}px`,
						"--flight-end-y": `${cartFlight.endY}px`,
						"--flight-delta-x": `${cartFlight.deltaX}px`,
						"--flight-delta-y": `${cartFlight.deltaY}px`,
						"--flight-half-delta-x": `${cartFlight.halfDeltaX}px`,
						"--flight-arc-delta-y": `${cartFlight.arcDeltaY}px`,
					}}
					onAnimationEnd={() => setCartFlight(null)}
				>
					<span aria-hidden="true">+</span>
				</div>
			)}
			<HashScroll />
		</>
	);
}

function App() {
	return (
		<BrowserRouter>
			<div className="app">
				<a className="skip-link" href="#conteudo">
					Pular para o conteúdo
				</a>
				<AppRoutes />
			</div>
		</BrowserRouter>
	);
}

export default App;