until [ \
  "$(curl -s -w '%{http_code}' -o /dev/null $1)" \
  -eq 200 ]
do
  sleep 5
done
