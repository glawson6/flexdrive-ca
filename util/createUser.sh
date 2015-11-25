use flexdrive
db.createCollection("OFFERINGS.PURCHASED")



db.createUser(
   {
     user: "flexUser",
     pwd: "flexUser",
     roles: [{role: "readWrite", db: "flexdrive" }]
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

use admin
db.createUser(
  {
    user: "glawson6",
    pwd: "Bigg6dogg!",
    roles: [ { role: "userAdminAnyDatabase", db: "admin" } ]
  }
)


db.createUser(
  {
    user: "flexAdmin",
    pwd: "Dr1v3Me!",
    roles: [ { role: "dbOwner", db: "flexdrive" } ]
  }
);

db.createUser(
  {
    user: "flexAdmin",
    pwd: "flexAdmin",
    roles: [ { role: "dbOwner", db: "flexdrive" } ]
  }
)

db.updateUser(
    "flexAdmin",
   {
     roles: [{role: "readWrite", db: "flexdrive" }]
   }
   );
db.runCommand( {
   dropUser: "flexadmin"
} )