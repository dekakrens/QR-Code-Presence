from Crypto.PublicKey import RSA
from Crypto.Cipher import PKCS1_v1_5
from base64 import b64decode

key = open("privateKey.pem").read()
key = key.replace("-----BEGIN RSA PRIVATE KEY-----", "").replace("-----END RSA PRIVATE KEY-----", "").replace("\n", "")
key = b64decode(key)
key = RSA.importKey(key)

inputString = input('Masukkan pesannya: ')
'''bL0jj5UDB4iQq3CcwCvVz37+3aBILUvgKM0GIoiBmM5A1GuTp1CZjxCIv0J6/l2jOrBT/4YmKFtXCrSSdIobfLiZeoHXLhm6ZmSxmHudcpFcUiGwqoochGY4TlsOgurHYNlBDvq1yh13rTBm3c3iSqVCSm5iVkFpTV8jrxtGUY4='''
cipher = PKCS1_v1_5.new(key)
plainText = cipher.decrypt(b64decode(inputString), "Error decrypting the input string!")

print(plainText)
