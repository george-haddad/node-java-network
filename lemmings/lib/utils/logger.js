const winston = require('winston');
const dotenv = require('dotenv');

dotenv.config({ silent: true });

const getWinstonLogger = () => {
  const logger = winston.createLogger({
    level: process.env.LOGGING_LEVEL || 'info',
    format: winston.format.json(),
    transports: [
      new winston.transports.File({
        filename: `./logs/error.log`,
        level: 'error',
      }),
      new winston.transports.File({
        filename: `./logs/combined.log`,
      }),
    ],
  });

  if (process.env.NODE_ENV !== 'production') {
    logger.add(
      new winston.transports.Console({
        format: winston.format.simple(),
      }),
    );
  }

  return logger;
};

module.exports = getWinstonLogger();
