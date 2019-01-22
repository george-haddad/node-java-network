const dotenv = require('dotenv');
const net = require('net');
const logger = require('./lib/utils/logger').Logger;

dotenv.config({ silent: true });

let app;

async function startApp() {
  app = net
    .createServer(socket => {
      socket.end('Bye bye\r\n');
    })
    .on('error', err => {
      if (err) {
        logger.error(`got error: ${err.name}`);
        logger.error(err);
      }
    });

  return app;
}

async function stopApp() {
  logger.info('Performing cleanups');
}

module.exports = {
  startApp,
  stopApp,
};
