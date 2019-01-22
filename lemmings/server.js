const dotenv = require('dotenv');
const { startApp, stopApp } = require('./app');
const logger = require('./lib/utils/logger').Logger;

dotenv.config({ silent: true });

const PORT = process.env.PORT || 3000;
let exiting = false;

startApp().then(app => {
  app.listen(PORT, () => {
    logger.info(`listening on port ${PORT}`);
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
    stopApp().then(() => {
      logger.info('Shutting down gracefully');
      process.exit(0);
    });
  }, 5000);
}

process.on('SIGINT', shutdown);
process.on('SIGTERM', shutdown);
