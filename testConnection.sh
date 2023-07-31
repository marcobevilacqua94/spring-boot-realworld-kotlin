until [ \
  "$(curl -s -w '%{http_code}' -o /dev/null "http://localhost:8080/api/articles")" \
  -eq 200 ]
do
  sleep 5
done


