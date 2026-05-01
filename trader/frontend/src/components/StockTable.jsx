import React, { useState } from 'react';

function StockTable({ stocks, onTrade }) {
  const [quantities, setQuantities] = useState({});

  const setQty = (id, val) => {
    setQuantities({ ...quantities, [id]: Math.max(1, parseInt(val) || 1) });
  };

  return (
    <div className="stock-table-container">
      <h2>Live Market</h2>
      <table className="stock-table">
        <thead>
          <tr>
            <th>Symbol</th>
            <th>Name</th>
            <th>Price</th>
            <th>Available</th>
            <th>Purchased %</th>
            <th>Quantity</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {stocks.map((stock) => (
            <tr key={stock.id}>
              <td className="symbol">{stock.symbol}</td>
              <td>{stock.name}</td>
              <td className="price">${Number(stock.price).toFixed(2)}</td>
              <td>{stock.availableShares.toLocaleString()}</td>
              <td>
                <div className="progress-bar">
                  <div className="progress-fill" style={{ width: `${stock.purchasePercentage}%` }}></div>
                  <span>{Number(stock.purchasePercentage).toFixed(1)}%</span>
                </div>
              </td>
              <td>
                <label htmlFor={`qty-${stock.id}`} className="sr-only">Quantity for {stock.symbol}</label>
                <input id={`qty-${stock.id}`} type="number" min="1" value={quantities[stock.id] || 1}
                  onChange={(e) => setQty(stock.id, e.target.value)} className="qty-input" />
              </td>
              <td className="actions">
                <button className="buy-btn" onClick={() => onTrade(stock.id, quantities[stock.id] || 1, 'buy')}>Buy</button>
                <button className="sell-btn" onClick={() => onTrade(stock.id, quantities[stock.id] || 1, 'sell')}>Sell</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

export default StockTable;
