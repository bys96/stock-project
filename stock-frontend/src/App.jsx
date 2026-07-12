import { useMemo, useState } from "react";
import { getHistory } from "./api/stockApi";
import "./App.css";

import DateSearch from "./components/DateSearch";
import MarketTabs from "./components/MarketTabs";
import StockChart from "./components/StockChart";
import StockDetail from "./components/StockDetail";

export default function App() {
  const today = new Date().toISOString().split("T")[0];

  const [loading, setLoading] = useState(false);
  const [selectedTab, setSelectedTab] = useState("ALL");
  const [selectedStock, setSelectedStock] = useState(null);
  const [hoverDate, setHoverDate] = useState(null);

  const [startDate, setStartDate] = useState("");
  const [endDate, setEndDate] = useState(today);

  const [kospiData, setKospiData] = useState([]);
  const [kosdaqData, setKosdaqData] = useState([]);

  const handleStartChange = (value) => {
    if (value && endDate && value > endDate) return;
    setStartDate(value);
  };

  const handleEndChange = (value) => {
    if (value && startDate && value < startDate) return;
    setEndDate(value);
  };

  const fetchData = async () => {
    if (!startDate || !endDate) return;

    const start = startDate.replaceAll("-", "");
    const end = endDate.replaceAll("-", "");

    setSelectedStock(null);
    setLoading(true);

    try {
      const [kospiRes, kosdaqRes] = await Promise.all([
        getHistory("KOSPI", start, end),
        getHistory("KOSDAQ", start, end),
      ]);

      setKospiData(kospiRes.data);
      setKosdaqData(kosdaqRes.data);
    } catch (e) {
      console.error(e);
      alert("조회 실패");
    } finally {
      setLoading(false);
    }
  };

  const chartData = useMemo(() => {
    if (selectedTab === "ALL") {
      const map = {};

      kospiData.forEach((item) => {
        map[item.tradeDate] = {
          date: item.tradeDate,
          kospi: item.closePrice,
          kosdaq: null,
        };
      });

      kosdaqData.forEach((item) => {
        if (!map[item.tradeDate]) {
          map[item.tradeDate] = {
            date: item.tradeDate,
            kospi: null,
            kosdaq: item.closePrice,
          };
        } else {
          map[item.tradeDate].kosdaq = item.closePrice;
        }
      });

      return Object.values(map).sort((a, b) => a.date.localeCompare(b.date));
    }

    if (selectedTab === "KOSPI") {
      return kospiData.map((item) => ({
        date: item.tradeDate,
        kospi: item.closePrice,
      }));
    }

    return kosdaqData.map((item) => ({
      date: item.tradeDate,
      kosdaq: item.closePrice,
    }));
  }, [selectedTab, kospiData, kosdaqData]);

  return (
    <div className="app-container">
      <div className="title">국내 주가지수</div>

      <DateSearch
        today={today}
        startDate={startDate}
        endDate={endDate}
        setStartDate={handleStartChange}
        setEndDate={handleEndChange}
        onSearch={fetchData}
        loading={loading}
      />

      <MarketTabs selectedTab={selectedTab} setSelectedTab={setSelectedTab} />

      <StockChart
        chartData={chartData}
        selectedTab={selectedTab}
        hoverDate={hoverDate}
        setHoverDate={setHoverDate}
        kospiData={kospiData}
        kosdaqData={kosdaqData}
        setSelectedStock={setSelectedStock}
        loading={loading}
      />

      <StockDetail selectedStock={selectedStock} selectedTab={selectedTab} />
    </div>
  );
}
