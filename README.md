# NanoShield 🔐

NanoShield is a lightweight, fast, and secure **inter-service authentication system** designed for API-to-API communication.

## 🚀 Features

- Stateless token structure
- Fingerprint + Timestamp + Nonce + HMAC
- Tokens expire in 30 seconds (configurable)
- Prevents replay attacks
- Retry mechanism for expired tokens

## 📦 Modules


- `service-a`: Token producer and sender service
- `service-b`: Token validator and secured endpoint

## 🔧 How to Run

1. Run `service-b` on port 8081
2. Run `service-a` on port 8080
3. Call: `http://localhost:8081/send`

## 📜 License

MIT
