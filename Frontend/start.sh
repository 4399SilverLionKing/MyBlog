# bin/bash
# /home/nginx/html 修改成你Nginx中静态文件的目录
nginx_dir='~/nginx/html'
# nginx 修改成你的容器的名称
docker stop nginx
echo '----stop nginx----'
rm -rf ${nginx_dir}
echo '----rm html dir----'
mv dist ${nginx_dir}
echo '----mv dist dir to html dir----'
# nginx 修改成你的容器的名称
docker start nginx
echo '----start nginx----'