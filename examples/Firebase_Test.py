import pyrebase

config = {
  "apiKey": "AIzaSyDdn1WGd0jSU2IjltZfiSx5zl5QeicPeng",
  "authDomain": "rasberrypi-ae90c.firebaseapp.com",
  "databaseURL": "https://rasberrypi-ae90c.firebaseio.com",
  "storageBucket": "rasberrypi-ae90c.appspot.com",
}

firebase = pyrebase.initialize_app(config)
db = firebase.database()
#data = {"name": "Mortimer 'Morty' Smith"}
#db.child("users").push(data)
#db.child("Data").push({"nama": "kelas",
 #                      "nim":"1234"})
db.child("users").child("coba").update({"name": "koko"})

#data = {"name": "Mortimer 'Morty' Smith"}
#db.child("users").child("Morty").set(data)
#data = {"name": "budi"}
#db.child("users").child("coba").set(data)
