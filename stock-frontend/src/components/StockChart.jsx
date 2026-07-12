import { ClipLoader } from "react-spinners";
import {
  ResponsiveContainer,
  LineChart,
  Line,
  XAxis,
  YAxis,
  Tooltip,
  Legend,
} from "recharts";

export default function StockChart({
  chartData,
  selectedTab,
  hoverDate,
  setHoverDate,
  kospiData,
  kosdaqData,
  setSelectedStock,
  loading,
}) {
  const handleClick = () => {
    if (!hoverDate) return;

    const kospi = kospiData.find((item) => item.tradeDate === hoverDate);

    const kosdaq = kosdaqData.find((item) => item.tradeDate === hoverDate);

    setSelectedStock({
      date: hoverDate,
      kospi,
      kosdaq,
    });
  };

  const dateFormatter = (value) =>
    `${value.slice(2, 4)}/${value.slice(4, 6)}/${value.slice(6, 8)}`;

  if (!chartData.length) {
    return <div className="chart-empty">조회된 데이터가 없습니다.</div>;
  }

  return loading ? (
    <div className="chart-loading">
      <ClipLoader size={50} color="#2563eb" />
      <p>데이터를 조회하는 중입니다...</p>
      <p>최초 조회 시 서버 시작으로 최대 1분 정도 소요될 수 있습니다.</p>
    </div>
  ) : (
    <ResponsiveContainer width="100%" height={450}>
      <LineChart
        className="stock-chart"
        data={chartData}
        tabIndex={-1}
        onMouseMove={(e) => {
          if (e.activeLabel) {
            setHoverDate(e.activeLabel);
          }
        }}
        onClick={handleClick}
      >
        <XAxis dataKey="date" tickFormatter={dateFormatter} />

        <YAxis />

        <Tooltip labelFormatter={dateFormatter} />

        <Legend />

        {selectedTab === "ALL" && (
          <>
            <Line
              type="monotone"
              dataKey="kospi"
              name="KOSPI"
              stroke="#ff7300"
              strokeWidth={2}
              dot={false}
            />

            <Line
              type="monotone"
              dataKey="kosdaq"
              name="KOSDAQ"
              stroke="#387908"
              strokeWidth={2}
              dot={false}
            />
          </>
        )}

        {selectedTab === "KOSPI" && (
          <Line
            type="monotone"
            dataKey="kospi"
            name="KOSPI"
            stroke="#ff7300"
            strokeWidth={2}
            dot={false}
          />
        )}

        {selectedTab === "KOSDAQ" && (
          <Line
            type="monotone"
            dataKey="kosdaq"
            name="KOSDAQ"
            stroke="#387908"
            strokeWidth={2}
            dot={false}
          />
        )}
      </LineChart>
    </ResponsiveContainer>
  );
}
