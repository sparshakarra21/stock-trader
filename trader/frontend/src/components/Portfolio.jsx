import React from 'react';

function Portfolio({ portfolio }) {
  return (
    <div className="portfolio-container">
      <h2>Your Portfolio</h2>
      {portfolio.holdings.length === 0 ? (
        <p className="empty-state">You don't own any stocks yet. Head to the Market to start trading.</p>
      ) : (
        <table className="stock-table">
          <thead>
            <tr>
              <th>Symbol</th>
              <th>Name</th>
              <th>Shares</th>
              <th>Current Price</th>
              <th>Total Value</th>
            </tr>
          </thead>
          <tbody>
            {portfolio.holdings.map((h, i) => (
              <tr key={i}>
                <td className="symbol">{h.stockSymbol}</td>
                <td>{h.stockName}</td>
                <td>{h.quantity}</td>
                <td>${Number(h.currentPrice).toFixed(2)}</td>
                <td className="price">${Number(h.totalValue).toFixed(2)}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}

export default Portfolio;
