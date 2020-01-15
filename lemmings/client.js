const WebSocket = require('ws');

const serverHost = 'ws://localhost:8888/lemmings/cave';
const webSocket = new WebSocket(serverHost);

async function startClient() {
  webSocket.on('open', () => {
    console.log(`Connected to: ${serverHost}`);
  });

  webSocket.on('message', data => {
    if (typeof data === 'string') {
      console.log(`(json) ws <- ${data}`);
    } else {
      const buff = new Uint8Array(data);
      console.log(`(binary) ws <- ${buff}`);
    }
  });

  webSocket.on('error', err => {
    console.log(err.message);
  });

  return webSocket;
}

async function stopClient() {
  console.log('Performing cleanups');
  webSocket.close(0, 'User closing connection');
}

startClient();

module.exports = {
  startClient,
  stopClient,
};
