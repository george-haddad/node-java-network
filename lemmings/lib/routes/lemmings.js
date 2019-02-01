const Router = require('koa-router');
const log = require('../utils/logger').Logger;

const router = new Router();

router.get('/api/lemmings', async ctx => {
  try {
    // const lemmings = await getLemmings();
    ctx.status = 200;
    ctx.body = {
      lemmings: 'yo yo yo',
      cave: 'yi yi yi',
    };
  } catch (err) {
    log.error(err);
  }
});

module.exports = router;
