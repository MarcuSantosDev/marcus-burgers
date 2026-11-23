# Frontend — Marcus's Burgers

Interface React da hamburgueria Marcus's Burgers. Utiliza Vite, React Router e integração com a API Spring Boot.

## Funcionalidades

- Catálogo por categorias e produtos.
- Carrinho persistido no navegador.
- Controle de quantidade conforme o estoque.
- Login, cadastro e perfil no cabeçalho.
- Busca automática de endereço pelo ViaCEP durante o checkout.
- Acompanhamento de pedidos por status.
- Painel administrativo em `/admin/pedidos`.

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
