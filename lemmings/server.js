const dotenv = require('dotenv');
const net = require('net');
const logger = require('./lib/utils/logger').Logger;

dotenv.config({ silent: true });

let server;

async function startServer() {
  server = net.createServer();
  server.on('error', err => {
    if (err) {
      logger.error(`got error: ${err.name}`);
      logger.error(err);
    }
  });

  server.on('connection', socket => {
    logger.info(`connection establish with ${socket.remoteAddress}`);
    socket.setEncoding('utf-8');
    socket.setTimeout(10000);
    socket.on('data', data => {
      logger.info(`received data: ${data}`);
    });
    socket.on('error', err => {
      logger.error(err);
    });
    socket.on('close', hadError => {
      logger.info(`Closing socket`);
      if (hadError) {
        logger.info('Yes it had an error');
      }
    });
  });

  return server;
}

async function stopServer() {
  logger.info('Performing cleanups');
}

module.exports = {
  startServer,
  stopServer,
};
