# Frontend — Marcus's Burgers

Interface React da hamburgueria Marcus's Burgers. Utiliza Vite, React Router e integração com a API Spring Boot.

## Funcionalidades

- Catálogo por categorias e produtos.
- Carrinho persistido no navegador para usuários clientes.
- Controle de quantidade conforme o estoque.
- Login, cadastro e perfil no cabeçalho.
- Busca automática de endereço pelo ViaCEP durante o checkout.
- Acompanhamento de pedidos por status.
- Painel administrativo de pedidos em `/admin/pedidos`.
- Cadastro, edição e exclusão de categorias em `/admin/categorias`.
- Cadastro, edição e exclusão de produtos em `/admin/produtos`.
- Administradores não veem carrinho, botão “Adicionar +” nem confirmação de pedido.
- Administradores podem abrir o checkout, mas não podem criar pedidos.

## Navegação por perfil

| Perfil   | Menu                                      | Tela após login  |
| -------- | ----------------------------------------- | ---------------- |
| `CLIENT` | Início, Cardápio, Sobre nós, Meus pedidos | `/checkout`      |
| `ADMIN`  | Categorias, Produtos, Pedidos             | `/admin/pedidos` |

## Executar

Na pasta `frontend`:

```powershell
npm install
npm run dev
```

Aplicação: `http://localhost:5173`

## API

Por padrão, o frontend se conecta em:

```text
http://localhost:8080
```

Uma URL diferente pode ser definida em `.env`:

```env
VITE_API_URL=http://localhost:8080
```

## Scripts

```powershell
npm run dev
npm run build
npm run lint
npm run preview
```

O build de produção é gerado em `dist`.
