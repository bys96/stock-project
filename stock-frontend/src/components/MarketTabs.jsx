export default function MarketTabs({ selectedTab, setSelectedTab }) {
  const tabs = [
    {
      key: "ALL",
      label: "전체",
    },
    {
      key: "KOSPI",
      label: "KOSPI",
    },
    {
      key: "KOSDAQ",
      label: "KOSDAQ",
    },
  ];

  return (
    <div className="market-tabs">
      {tabs.map((tab) => (
        <div
          key={tab.key}
          onClick={() => setSelectedTab(tab.key)}
          className={`market-tab ${selectedTab === tab.key ? "active" : ""}`}
        >
          {tab.label}
        </div>
      ))}
    </div>
  );
}
