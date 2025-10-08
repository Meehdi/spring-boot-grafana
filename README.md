# 📊 Spring Boot + Grafana Monitoring Stack

A complete, production-ready monitoring solution for Spring Boot applications using Prometheus and Grafana. Get beautiful real-time dashboards in minutes!

![GitHub](https://img.shields.io/github/license/meehdi/spring-boot-grafana)
![Java](https://img.shields.io/badge/Java-17+-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen)
![Docker](https://img.shields.io/badge/docker compose-blue)

## 🌟 Features

- ✅ **Real-time metrics** - Monitor your application performance live
- ✅ **Pre-configured dashboards** - Professional visualizations out of the box
- ✅ **Custom metrics** - Track business logic and technical metrics
- ✅ **Docker Compose** - One command to start everything
- ✅ **Production-ready** - Security and best practices included
- ✅ **Easy to extend** - Add your own metrics and dashboards

## 🚀 Quick Start

### Prerequisites

- Java 17+
- Docker & Docker Compose
- Gradle (included via wrapper)

### Start the Stack

```bash
# Clone the repository
git clone https://github.com/yourusername/spring-boot-grafana.git
cd spring-boot-grafana

# Start all services
docker compose up -d

# Wait ~30 seconds for everything to initialize
```

### Access the Services

- **Spring Boot App**: http://localhost:8080
- **Prometheus**: http://localhost:9090
- **Grafana**: http://localhost:3000 (admin/admin)

That's it! 🎉

## 📁 Project Structure

```
spring-boot-grafana/
├── spring-boot-app/              # Spring Boot application
│   ├── src/
│   │   └── main/
│   │       ├── java/
│   │       │   └── com/meehdi/demo/
│   │       │       ├── controller/
│   │       │       │   └── MetricsController.java
│   │       │       └── config/
│   │       │           └── CustomMetricsConfig.java
│   │       └── resources/
│   │           └── application.yml
│   ├── build.gradle
│   └── Dockerfile
├── docker/                       # Docker configurations
│   ├── prometheus/
│   │   └── prometheus.yml       # Prometheus scraping config
│   └── grafana/
│       └── provisioning/
│           └── datasources/
│               └── datasource.yml
├── docker compose.yml           # Orchestration file
├── test-endpoints.sh            # Load testing script
└── README.md
```

## 🧪 Testing the Endpoints

### Manual Testing

```bash
# Health check
curl http://localhost:8080/actuator/health

# Simple endpoint
curl http://localhost:8080/api/hello

# Random success/error endpoint
curl http://localhost:8080/api/random

# View current statistics
curl http://localhost:8080/api/metrics/stats

# View raw Prometheus metrics
curl http://localhost:8080/actuator/prometheus
```

### Load Testing

Generate realistic traffic to populate your dashboards:

```bash
chmod +x test-endpoints.sh
./test-endpoints.sh
```

This script will continuously send requests to various endpoints, creating data for your Grafana dashboards.

## 📊 Setting Up Grafana Dashboards

### Import Pre-Built Dashboards

1. Open Grafana at http://localhost:3000
2. Login with `admin` / `admin` (change password when prompted)
3. Click **"+"** → **"Import"**
4. Enter one of these dashboard IDs:
    - **4701** - JVM Micrometer
    - **11378** - Spring Boot Statistics
5. Select **Prometheus** as the data source
6. Click **Import**

### View Logs

```bash
# All services
docker compose logs -f

# Specific service
docker compose logs -f spring-boot-app
docker compose logs -f prometheus
docker compose logs -f grafana
```

## 🚨 Troubleshooting

### Prometheus Can't Connect to Spring Boot

**Check if the app is running:**
```bash
curl http://localhost:8080/actuator/health
```

**Check Prometheus targets:**
- Go to http://localhost:9090/targets
- Look for `spring-boot-app` - should show **"UP"**

**Check Docker networking:**
```bash
docker exec prometheus ping spring-boot-app
```

### Grafana Shows "No Data"

1. Verify Prometheus is collecting metrics:
    - Go to http://localhost:9090
    - Query: `api_requests_total`
    - Should show results

2. Check Grafana data source:
    - Configuration → Data Sources → Prometheus
    - Click "Save & Test" - should show green checkmark

3. Verify time range in Grafana (top right corner)

### Can't Access Services

**Check if containers are running:**
```bash
docker compose ps
```

**Restart everything:**
```bash
docker compose down
docker compose up -d
```

## 🔒 Production Considerations

Before deploying to production:

- [ ] Change Grafana admin password
- [ ] Limit exposed actuator endpoints
- [ ] Add authentication to Prometheus and Grafana
- [ ] Set up HTTPS/TLS
- [ ] Configure resource limits in docker compose
- [ ] Set up backup for Grafana dashboards
- [ ] Configure alerting rules
- [ ] Use persistent volumes for data

### Example Production application.yml

```yaml
management:
  endpoints:
    web:
      exposure:
        include: prometheus,health  # Only essentials
  endpoint:
    health:
      show-details: never  # Don't expose details
```

## 📚 Learn More

- [Medium Article](https://medium.com/@elmehdi.kzadri_97626/monitor-your-spring-boot-app-like-a-pro-with-grafana-in-just-15-minutes-fe046bdd16fd) - Detailed tutorial
- [Micrometer Documentation](https://micrometer.io/docs)
- [Prometheus Documentation](https://prometheus.io/docs/)
- [Grafana Documentation](https://grafana.com/docs/)
- [Spring Boot Actuator](https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html)

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## ⭐ Show Your Support

If this project helped you, please give it a ⭐️!

## 👤 Author

- GitHub: [@Meehdi](https://github.com/Meehdi)
- Medium: [@elmehdi.kzadri](https://medium.com/@elmehdi.kzadri_97626)
- LinkedIn: [El Mehdi KZADRI](https://linkedin.com/in/elmehdikzadri)

**Made with ❤️ and ☕ by developers, for developers**