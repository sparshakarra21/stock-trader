import React, { useState, useEffect } from 'react';
import { getStocks, getPortfolio, buyStock, sellStock, getTransactions } from '../services/api';
import { connectWebSocket, disconnectWebSocket } from '../services/websocket';
import StockTable from './StockTable';
import Portfolio from './Portfolio';
import TransactionHistory from './TransactionHistory';

function Dashboard({ username, onLogout }) {
  const [stocks, setStocks] = useState([]);
  const [portfolio, setPortfolio] = useState({ balance: 0, holdings: [] });
  const [transactions, setTransactions] = useState([]);
  const [activeTab, setActiveTab] = useState('market');
  const [message, setMessage] = useState('');

  useEffect(() => {
    loadData();
    connectWebSocket((updatedStocks) => setStocks(updatedStocks));
    return () => disconnectWebSocket();
  }, []);

  const loadData = async () => {
    try {
      const [stockRes, portfolioRes, txRes] = await Promise.all([
        getStocks(), getPortfolio(), getTransactions()
      ]);
      setStocks(stockRes.data);
      setPortfolio(portfolioRes.data);
      setTransactions(txRes.data);
    } catch (err) {
      console.error('Failed to load data', err);
    }
  };

  const handleTrade = async (stockId, quantity, type) => {
    try {
      if (type === 'buy') await buyStock({ stockId, quantity });
      else await sellStock({ stockId, quantity });
      setMessage(`${type === 'buy' ? 'Purchase' : 'Sale'} successful!`);
      loadData();
      setTimeout(() => setMessage(''), 3000);
    } catch (err) {
      setMessage(err.response?.data?.error || 'Trade failed');
      setTimeout(() => setMessage(''), 3000);
    }
  };

  return (
    <div className="dashboard">
      <header className="dashboard-header">
        <h1>📈 Stock Trading</h1>
        <div className="header-right">
          <span>Welcome, {username}</span>
          <span className="balance">Balance: ${Number(portfolio.balance).toFixed(2)}</span>
          <button onClick={onLogout} className="logout-btn">Logout</button>
        </div>
      </header>

      {message && <div className="toast" role="status">{message}</div>}

      <nav className="tabs" role="tablist">
        <button role="tab" aria-selected={activeTab === 'market'} className={activeTab === 'market' ? 'active' : ''} onClick={() => setActiveTab('market')}>Market</button>
        <button role="tab" aria-selected={activeTab === 'portfolio'} className={activeTab === 'portfolio' ? 'active' : ''} onClick={() => setActiveTab('portfolio')}>Portfolio</button>
        <button role="tab" aria-selected={activeTab === 'history'} className={activeTab === 'history' ? 'active' : ''} onClick={() => setActiveTab('history')}>History</button>
      </nav>

      <main className="content" role="tabpanel">
        {activeTab === 'market' && <StockTable stocks={stocks} onTrade={handleTrade} />}
        {activeTab === 'portfolio' && <Portfolio portfolio={portfolio} />}
        {activeTab === 'history' && <TransactionHistory transactions={transactions} />}
      </main>
    </div>
  );
}

export default Dashboard;
