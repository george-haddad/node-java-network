const dotenv = require('dotenv');
const { startServer, stopServer } = require('./server');
const logger = require('./lib/utils/logger').Logger;

dotenv.config({ silent: true });

const PORT = process.env.PORT || 3000;
const HOST = process.env.HOST || 'localhost';
let exiting = false;

startServer().then(app => {
  app.listen(PORT, HOST, () => {
    logger.info(`listening on ${HOST}:${PORT}`);
  });
});

function shutdown() {
  logger.info('Received SIGINT or SIGTERM');

  if (exiting) {
    return;
  }

  exiting = true;
  logger.info('Attempting to gracefully shutdown the server');

  setTimeout(() => {
    stopServer().then(() => {
      logger.info('Shutting down gracefully');
      process.exit(0);
    });
  }, 5000);
}

process.on('SIGINT', shutdown);
process.on('SIGTERM', shutdown);
