import base64
import hashlib

password = b"password123"

users = {
    'admin': ('ySH1msblDV9A5mIhccyCyA==', 'qpNUJGRQUR2j/NDJ9RR0HjIPqqGingQMxu9zgbQMDOQ='),
    'manager': ('HtP9svA7PCLX+KbNSg6SLg==', 'GrYU4rhzbSwCeRRWVLAePvk22fZ/CyukvK0LztFHbo8='),
    'staff': ('+v2UFEWbu2OH5fjtrrg/2A==', 'B7gfAxygBXVOpVmuTkJQybApgOSOJVTrxE3whcIJ2Zs='),
    'sales': ('Y+til+fcii/WiLxxWssyOg==', '9kNxa9TkKpG8Hz59fOr9QX9PbCgisbbSOAtKEJJbB/U='),
    'testmanager': ('HtP9svA7PCLX+KbNSg6SLg==', 'GrYU4rhzbSwCeRRWVLAePvk22fZ/CyukvK0LztFHbo8='),
    'teststaff': ('+v2UFEWbu2OH5fjtrrg/2A==', 'B7gfAxygBXVOpVmuTkJQybApgOSOJVTrxE3whcIJ2Zs=')
}

all_ok = True
for username, (salt_b64, hash_b64) in users.items():
    salt = base64.b64decode(salt_b64)
    expected_hash = base64.b64decode(hash_b64)
    h = hashlib.sha256(salt + password).digest()  # SHA-256(salt || password)
    ok = h == expected_hash
    print(f"{username}: {'OK' if ok else 'MISMATCH'}")
    if not ok:
        print(f"  expected: {hash_b64}\n  actual:   {base64.b64encode(h).decode()}")
        all_ok = False

print('\nOverall:', 'PASS' if all_ok else 'FAIL')
