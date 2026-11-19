import { useState } from "react";
import { useCart } from "./hooks/useCart";
import Header from "./components/Header/Header";
import Hero from "./components/Hero/Hero";
import Menu from "./components/Menu/Menu";
import About from "./components/About/About";
import Footer from "./components/Footer/Footer";
import CartDrawer from "./components/CartDrawer/CartDrawer";
import "./App.css";

function App() {
  const cart = useCart();
  const [isCartOpen, setIsCartOpen] = useState(false);

  return (
    <div className="app">
      <a className="skip-link" href="#conteudo">
        Pular para o conteúdo
      </a>

      <Header
        cartCount={cart.totalItems}
        onOpenCart={() => setIsCartOpen(true)}
      />

      <main id="conteudo">
        <Hero />
        <Menu onAddToCart={(product) => cart.add(product.id)} />
        <About />
      </main>

      <Footer />

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
      />
    </div>
  );
}

export default App;