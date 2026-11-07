"""Validate the cloud-produced demo artifact; never prints config/key contents."""
import json
from pathlib import Path
import subprocess
import sys
import zipfile

apk, analyzer = sys.argv[1:3]
permissions = subprocess.check_output([analyzer, 'manifest', 'permissions', apk], text=True)
for permission in ['android.permission.INTERNET', 'android.permission.ACCESS_NETWORK_STATE']:
    if permission in permissions:
        raise SystemExit('Demo unexpectedly requests a network permission')
application_id = subprocess.check_output([analyzer, 'manifest', 'application-id', apk], text=True).strip()
if application_id != 'com.example.smartroutinesystem.demo':
    raise SystemExit('Demo must have its own application ID')
config = json.loads(Path('../app/google-services.json').read_text())
markers = [b'com/google/firebase', b'com.google.firebase', config['project_info']['project_id'].encode()]
for client in config.get('client', []):
    for key in client.get('api_key', []):
        markers.append(key['current_key'].encode())
with zipfile.ZipFile(apk) as archive:
    for entry in archive.namelist():
        contents = archive.read(entry)
        if any(marker in contents for marker in markers):
            raise SystemExit('Firebase code/configuration unexpectedly packaged in demo')
print('APK verified: distinct demo package, no Internet permission, no Firebase SDK/configuration.')
