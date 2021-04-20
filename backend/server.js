const express = require("express");

const app = express();
app.use(express.json());

require("./routes/user")(app);
require("./routes/article")(app);

require("./scrap");

const port = process.env.PORT || 3000;

app.listen(port, () => {
  console.log(`application listening on port ${port}`);
});
