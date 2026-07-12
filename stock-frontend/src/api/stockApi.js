import axios from "axios";

const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL,
});

export const getHistory = (market, startDate, endDate) => {
  return api.get("/index/history", {
    params: {
      market,
      startDate,
      endDate,
    },
  });
};
