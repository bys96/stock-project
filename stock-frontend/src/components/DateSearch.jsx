import "../App.css";

export default function DateSearch({
  today,
  startDate,
  endDate,
  setStartDate,
  setEndDate,
  onSearch,
  loading,
}) {
  const openCalendar = (e) => {
    if (loading) return;
    e.target.showPicker?.();
    e.target.focus();
  };

  return (
    <div className="date-search">
      <span className="date-search-label">기간</span>
      <input
        type="date"
        value={startDate}
        onChange={(e) => setStartDate(e.target.value)}
        onClick={openCalendar}
        max={endDate || today}
        disabled={loading}
      />

      <span>~</span>

      <input
        type="date"
        value={endDate}
        onChange={(e) => setEndDate(e.target.value)}
        onClick={openCalendar}
        min={startDate || undefined}
        max={today}
        disabled={loading}
      />

      <button className="date-search-btn" onClick={onSearch} disabled={loading}>
        조회
      </button>
    </div>
  );
}
