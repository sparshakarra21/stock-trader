import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';

let stompClient = null;

export const connectWebSocket = (onStockUpdate) => {
  stompClient = new Client({
    webSocketFactory: () => new SockJS(`${process.env.REACT_APP_WS_URL || 'http://localhost:8080'}/ws`),
    onConnect: () => {
      stompClient.subscribe('/topic/stocks', (message) => {
        const stocks = JSON.parse(message.body);
        onStockUpdate(stocks);
      });
    },
    reconnectDelay: 5000,
  });
  stompClient.activate();
};

export const disconnectWebSocket = () => {
  if (stompClient) stompClient.deactivate();
};
