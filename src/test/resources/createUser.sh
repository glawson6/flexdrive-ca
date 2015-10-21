use flexdrive
db.createUser(
   {
     user: "flexAdmin",
     pwd: "flexAdmin",
     roles: [{role: "readWrite", db: "flexdrive" },{role: "readWrite", db: "dbAdmin"}]
   }
)
db.runCommand( {
   dropUser: "flexAdmin"
} )
db.updateUser(
    "flexAdmin",
   {
     pwd: "flexAdmin",
     roles: [{role: "readWrite", db: "flexdrive" },{role: "readWrite", db: "dbAdmin"}]
   }
)

