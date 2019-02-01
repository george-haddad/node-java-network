const Koa = require('koa');
const logger = require('koa-logger');
const dotenv = require('dotenv');
const WebSocket = require('ws');

const indexRoutes = require('./lib/routes/index');
const lemmingsRoutes = require('./lib/routes/lemmings');
const log = require('./lib/utils/logger');

dotenv.config({ silent: true });

const app = new Koa();
const { HOST, PORT, WS_HOST, WS_PORT } = process.env;

app.use(logger());
app.use(indexRoutes.routes());
app.use(lemmingsRoutes.routes());

const server = app.listen(PORT, HOST, () => {
  log.info(`server listening on ${HOST}:${PORT}`);
});

const ws = new WebSocket(`ws://${WS_HOST}:${WS_PORT}/lemmings/cave`);
ws.on('open', () => {
  log.info(`server connected to ws://${WS_HOST}:${WS_PORT}/lemmings/cave`);
  ws.send('road to lemmings life connected');
});

ws.on('message', data => {
  if (data) {
    const lemming = data.message;
    log.info(`lemming from cave: ${lemming}`);
  }
});

ws.on('close', (code, reason) => {
  log.info(`server disconnected from ws://${WS_HOST}:${WS_PORT}/lemmings/cave`);
  log.info(`closed ws (${code}) ${reason}`);
});

ws.on('error', err => {
  log.error(`ws error: ${err}`);
});

module.exports = server;
