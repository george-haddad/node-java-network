const WebSocket = require('ws');
const logger = require('./lib/utils/logger').Logger;

const serverHost = 'ws://localhost:8888/lemmings/cave';
const webSocket = new WebSocket(serverHost);

async function startClient() {
  webSocket.on('open', () => {
    logger.info(`Connected to: ${serverHost}`);
  });

  webSocket.on('message', data => {
    if (typeof data === 'string') {
      logger.info(`(json) ws <- ${data}`);
    } else {
      const buff = new Uint8Array(data);
      logger.info(`(binary) ws <- ${buff}`);
    }
  });

  webSocket.on('error', err => {
    logger.info(err.message);
  });

  return webSocket;
}

async function stopClient() {
  logger.info('Performing cleanups');
  webSocket.close(0, 'User closing connection');
}

startClient();

module.exports = {
  startClient,
  stopClient,
};
