const express = require("express");

const app = express();
app.use(express.json());

require("./routes/user")(app);
require("./routes/article")(app);

require("./scrap");

app.listen(3000, () => {
  console.log("application listening on port 3000");
});
