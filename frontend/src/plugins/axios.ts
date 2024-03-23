import axios from "axios";

const apiClient = axios.create({
  baseURL: import.meta.env.VITE_BASE_URL || "http://127.0.0.1:8080/api",
});

export default apiClient;
