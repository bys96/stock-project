import {
  formatDate,
  formatNumber,
  formatRate,
  getRateColor,
} from "../utils/stockFormatter";

export default function StockDetail({ selectedStock, selectedTab }) {
  if (!selectedStock) return null;

  const stocks = [
    {
      name: "KOSPI",
      color: "#2563eb",
      data: selectedStock.kospi,
    },
    {
      name: "KOSDAQ",
      color: "#16a34a",
      data: selectedStock.kosdaq,
    },
  ];

  return (
    <div className="stock-detail">
      <h3 className="stock-detail-date">{formatDate(selectedStock.date)}</h3>

      <div className="stock-detail-list">
        {stocks
          .filter((item) => {
            if (!item.data) return false;

            if (selectedTab === "ALL") return true;

            return item.name === selectedTab;
          })
          .map((item) => (
            <div key={item.name} className="stock-card">
              <div
                className="stock-card-header"
                style={{
                  background: item.color,
                }}
              >
                {item.name}
              </div>

              <div className="stock-card-body">
                <p>
                  시가 : <strong>{formatNumber(item.data.openPrice)}</strong>
                </p>

                <p>
                  고가 : <strong>{formatNumber(item.data.highPrice)}</strong>
                </p>

                <p>
                  저가 : <strong>{formatNumber(item.data.lowPrice)}</strong>
                </p>

                <p>
                  종가 : <strong>{formatNumber(item.data.closePrice)}</strong>
                </p>

                <p>
                  등락률 :
                  <strong
                    style={{
                      color: getRateColor(item.data.changeRate),
                    }}
                  >
                    {" "}
                    {formatRate(item.data.changeRate)}
                  </strong>
                </p>

                <p>
                  거래량 : <strong>{formatNumber(item.data.volume)}</strong>
                </p>
              </div>
            </div>
          ))}
      </div>
    </div>
  );
}
