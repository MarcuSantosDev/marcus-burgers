import { useEffect, useState } from "react";
import { Navigate } from "react-router-dom";
import { api, getUserProfile } from "../services/api";
import "./Pages.css";

export function AdminCategoriesPage() {
  const profile = getUserProfile();
  const [categories, setCategories] = useState([]);
  const [form, setForm] = useState({ name: "", description: "" });
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);
  const [editingId, setEditingId] = useState(null);

  async function loadCategories() {
    try {
      setCategories(await api.categories());
    } catch (requestError) {
      setError(requestError.message);
    }
  }

  useEffect(() => {
    let cancelled = false;

    api.categories()
      .then((items) => {
        if (!cancelled) setCategories(items);
      })
      .catch((requestError) => {
        if (!cancelled) setError(requestError.message);
      });

    return () => {
      cancelled = true;
    };
  }, []);

  async function submit(event) {
    event.preventDefault();
    setLoading(true);
    setError("");
    try {
      if (editingId) {
        await api.updateCategory(editingId, form);
      } else {
        await api.createCategory(form);
      }
      setForm({ name: "", description: "" });
      setEditingId(null);
      await loadCategories();
    } catch (requestError) {
      setError(requestError.message);
    } finally {
      setLoading(false);
    }
  }

  if (profile?.role !== "ADMIN") return <Navigate to="/" replace />;

  return (
    <main className="page">
      <p className="eyebrow">ÁREA ADMINISTRATIVA</p>
      <h1 className="page__title">Categorias</h1>

      <div className="admin-catalog-grid">
        <section className="panel">
          <h2>Cadastrar categoria</h2>
          <form className="form" onSubmit={submit}>
            <label>
              Nome
              <input
                name="name"
                value={form.name}
                onChange={(event) => setForm((current) => ({ ...current, name: event.target.value }))}
                maxLength={100}
                required
              />
            </label>
            <label>
              Descrição
              <textarea
                name="description"
                value={form.description}
                onChange={(event) => setForm((current) => ({ ...current, description: event.target.value }))}
                rows="4"
              />
            </label>
            {error && <p className="form__error" role="alert">{error}</p>}
            <button className="primary-button" type="submit" disabled={loading || !form.name.trim()}>
              {loading ? "Salvando..." : "Cadastrar categoria"}
            </button>
          </form>
        </section>

        <section className="panel">
          <h2>Categorias cadastradas</h2>
          {categories.length === 0 ? (
            <p className="empty-state">Nenhuma categoria cadastrada.</p>
          ) : (
            <ul className="admin-catalog-list">
              {categories.map((category) => (
                <li key={category.categoryId}>
                  <div>
                    <strong>{category.name}</strong>
                    {category.description && <small>{category.description}</small>}
                  </div>
                  <div className="admin-catalog-actions">
                    <button
                      type="button"
                      className="secondary-button"
                      onClick={() => {
                        setEditingId(category.categoryId);
                        setForm({ name: category.name, description: category.description || "" });
                        setError("");
                      }}
                    >Editar</button>
                    <button
                      type="button"
                      className="secondary-button"
                      onClick={async () => {
                        if (!window.confirm(`Excluir a categoria "${category.name}"?`)) return;
                        setLoading(true);
                        setError("");
                        try {
                          await api.deleteCategory(category.categoryId);
                          await loadCategories();
                        } catch (requestError) {
                          setError(requestError.message);
                        } finally {
                          setLoading(false);
                        }
                      }}
                    >Excluir</button>
                  </div>
                </li>
              ))}
            </ul>
          )}
        </section>
      </div>
    </main>
  );
}

export function AdminProductsPage() {
  const profile = getUserProfile();
  const [products, setProducts] = useState([]);
  const [categories, setCategories] = useState([]);
  const [form, setForm] = useState({
    name: "",
    description: "",
    imageUrl: "",
    price: "",
    stock: "",
    categoryId: "",
  });
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);
  const [editingId, setEditingId] = useState(null);

  async function loadCatalog() {
    try {
      const [productList, categoryList] = await Promise.all([
        api.products(),
        api.categories(),
      ]);
      setProducts(productList);
      setCategories(categoryList);
    } catch (requestError) {
      setError(requestError.message);
    }
  }

  useEffect(() => {
    let cancelled = false;

    Promise.all([api.products(), api.categories()])
      .then(([productList, categoryList]) => {
        if (cancelled) return;
        setProducts(productList);
        setCategories(categoryList);
      })
      .catch((requestError) => {
        if (!cancelled) setError(requestError.message);
      });

    return () => {
      cancelled = true;
    };
  }, []);

  async function submit(event) {
    event.preventDefault();
    setLoading(true);
    setError("");
    try {
      const payload = {
        name: form.name,
        description: form.description,
        imageUrl: form.imageUrl,
        price: Number(form.price),
        stock: Number(form.stock),
        categoryId: Number(form.categoryId),
      };
      if (editingId) {
        await api.updateProduct(editingId, payload);
      } else {
        await api.createProduct(payload);
      }
      setForm({ name: "", description: "", imageUrl: "", price: "", stock: "", categoryId: "" });
      setEditingId(null);
      await loadCatalog();
    } catch (requestError) {
      setError(requestError.message);
    } finally {
      setLoading(false);
    }
  }

  if (profile?.role !== "ADMIN") return <Navigate to="/" replace />;

  return (
    <main className="page">
      <p className="eyebrow">ÁREA ADMINISTRATIVA</p>
      <h1 className="page__title">Produtos</h1>

      <div className="admin-catalog-grid">
        <section className="panel">
          <h2>{editingId ? "Editar produto" : "Cadastrar produto"}</h2>
          <form className="form" onSubmit={submit}>
            <label>
              Nome
              <input name="name" value={form.name} onChange={(event) => setForm((current) => ({ ...current, name: event.target.value }))} maxLength={100} required />
            </label>
            <label>
              Descrição
              <textarea name="description" value={form.description} onChange={(event) => setForm((current) => ({ ...current, description: event.target.value }))} rows="3" />
            </label>
            <label>
              URL da imagem
              <input name="imageUrl" type="url" value={form.imageUrl} onChange={(event) => setForm((current) => ({ ...current, imageUrl: event.target.value }))} />
            </label>
            <div className="form-row">
              <label>
                Preço
                <input name="price" type="number" min="0" step="0.01" value={form.price} onChange={(event) => setForm((current) => ({ ...current, price: event.target.value }))} required />
              </label>
              <label>
                Estoque
                <input name="stock" type="number" min="0" step="1" value={form.stock} onChange={(event) => setForm((current) => ({ ...current, stock: event.target.value }))} required />
              </label>
            </div>
            <label>
              Categoria
              <select name="categoryId" value={form.categoryId} onChange={(event) => setForm((current) => ({ ...current, categoryId: event.target.value }))} required>
                <option value="">Selecione uma categoria</option>
                {categories.map((category) => (
                  <option key={category.categoryId} value={category.categoryId}>{category.name}</option>
                ))}
              </select>
            </label>
            {error && <p className="form__error" role="alert">{error}</p>}
            <button className="primary-button" type="submit" disabled={loading || !form.name.trim() || !form.categoryId}>
              {loading ? "Salvando..." : editingId ? "Salvar alterações" : "Cadastrar produto"}
            </button>
            {editingId && (
              <button
                type="button"
                className="secondary-button"
                onClick={() => {
                  setEditingId(null);
                  setForm({ name: "", description: "", imageUrl: "", price: "", stock: "", categoryId: "" });
                  setError("");
                }}
              >Cancelar edição</button>
            )}
          </form>
        </section>

        <section className="panel">
          <h2>Produtos cadastrados</h2>
          {products.length === 0 ? (
            <p className="empty-state">Nenhum produto cadastrado.</p>
          ) : (
            <ul className="admin-catalog-list">
              {products.map((product) => (
                <li key={product.productId}>
                  <div>
                    <strong>{product.name}</strong>
                    <small>{product.category?.name || "Sem categoria"} · Estoque: {product.stock}</small>
                  </div>
                  <div className="admin-catalog-actions">
                    <button
                      type="button"
                      className="secondary-button"
                      onClick={() => {
                        setEditingId(product.productId);
                        setForm({
                          name: product.name,
                          description: product.description || "",
                          imageUrl: product.imageUrl || "",
                          price: String(product.price),
                          stock: String(product.stock),
                          categoryId: String(product.category?.categoryId || ""),
                        });
                        setError("");
                        window.scrollTo({ top: 0, behavior: "smooth" });
                      }}
                    >Editar</button>
                    <button
                      type="button"
                      className="secondary-button"
                      onClick={async () => {
                        if (!window.confirm(`Excluir o produto "${product.name}"?`)) return;
                        setLoading(true);
                        setError("");
                        try {
                          await api.deleteProduct(product.productId);
                          await loadCatalog();
                        } catch (requestError) {
                          setError(requestError.message);
                        } finally {
                          setLoading(false);
                        }
                      }}
                    >Excluir</button>
                  </div>
                </li>
              ))}
            </ul>
          )}
        </section>
      </div>
    </main>
  );
}
