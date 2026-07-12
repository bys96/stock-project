export const formatDate = (date) => {
  if (!date) return "";

  return `${date.slice(0, 4)}.${date.slice(4, 6)}.${date.slice(6, 8)}`;
};

export const formatNumber = (value) => {
  if (value == null) return "-";

  return Number(value).toLocaleString();
};

export const formatRate = (value) => {
  if (value == null) return "-";

  return `${Number(value).toFixed(2)}%`;
};

export const getRateColor = (value) => {
  if (value > 0) return "#dc2626";

  if (value < 0) return "#2563eb";

  return "#666";
};
