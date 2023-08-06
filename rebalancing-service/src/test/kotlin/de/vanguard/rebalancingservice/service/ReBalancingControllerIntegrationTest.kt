package de.vanguard.rebalancingservice.service

import de.vanguard.rebalancingservice.AbstractIntegrationTest
import de.vanguard.rebalancingservice.service.client.CustomerPortfolioDto
import de.vanguard.rebalancingservice.service.client.CustomerTradeDto
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.mockserver.model.HttpRequest
import org.mockserver.model.HttpResponse
import org.mockserver.model.MediaType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus


internal class ReBalancingControllerIntegrationTest @Autowired constructor(private val restTemplate: TestRestTemplate) : AbstractIntegrationTest() {

    private val url = "/api/re-balancing"


    @Test
    internal fun `should be able to get the strategies for a particular customer`() {

        restTemplate.postForEntity(url.plus("/load/daily/customers-strategies"), null, Void::class.java)

        val customerId = "1"
        val strategies = restTemplate.getForEntity(url.plus("/customer/").plus(customerId).plus("/strategies/"), Array<StrategyDto>::class.java).body
        Assertions.assertThat(strategies!!.isNotEmpty())

    }

    @Test
    internal fun `should be able to get all the customers`() {
        restTemplate.postForEntity(url.plus("/load/daily/customers-strategies"), null, Void::class.java)

        val customers = restTemplate.getForEntity(url.plus("/customers/all"), Array<CustomerDto>::class.java).body
        Assertions.assertThat(customers!!.isNotEmpty())
    }

    @Test
    internal fun `should be able to get all the strategies`() {
        restTemplate.postForEntity(url.plus("/load/daily/customers-strategies"), null, Void::class.java)

        val customers = restTemplate.getForEntity(url.plus("/strategies/all"), Array<StrategyDto>::class.java).body
        Assertions.assertThat(customers!!.isNotEmpty())
    }


    @Test
    internal fun `should be able to update the strategy for a particular customer`() {
        restTemplate.postForEntity(url.plus("/load/daily/customers-strategies"), null, Void::class.java)
        val customerId = "1"
        val strategies = restTemplate.getForEntity(
            url.plus("/customer/").plus(customerId).plus("/strategies/"),
            Array<StrategyDto>::class.java
        ).body
        assert(strategies!!.isNotEmpty())

        val responseEntity = restTemplate.postForEntity(
            url.plus("/customer/").plus(customerId).plus("/strategy/").plus(strategies[0].strategyId),
            null,
            Void::class.java
        )

        Assertions.assertThat(responseEntity.statusCode.is2xxSuccessful)
        Assertions.assertThat(responseEntity.statusCode == HttpStatus.ACCEPTED)

    }

    @Test
    internal fun `should get HttpStatus_NOT_FOUND while updating an invalid strategy for a particular customer`() {
        restTemplate.postForEntity(url.plus("/load/daily/customers-strategies"), null, Void::class.java)
        val customerId = "1"
        val strategies = restTemplate.getForEntity(
            url.plus("/customer/").plus(customerId).plus("/strategies/"),
            Array<StrategyDto>::class.java
        ).body
        assert(strategies!!.isNotEmpty())

        val responseEntity = restTemplate.postForEntity(
            url.plus("/customer/").plus(customerId).plus("/strategy/").plus("INVALID"),
            null,
            Void::class.java
        )

        Assertions.assertThat(responseEntity.statusCode == HttpStatus.NOT_FOUND)
    }

    @Test
    internal fun `should get HttpStatus_BAD_REQUEST while updating an invalid strategy for a particular customer`() {
        val customerId = "1"
        restTemplate.postForEntity(url.plus("/load/daily/customers-strategies"), null, Void::class.java)

        val strategies = restTemplate.getForEntity(
            url.plus("/customer/").plus(customerId).plus("/strategies/"),
            Array<StrategyDto>::class.java
        ).body
        assert(strategies!!.isNotEmpty())

        val responseEntity = restTemplate.postForEntity(
            url.plus("/customer/").plus(customerId).plus("/strategy/").plus("2"),
            null,
            Void::class.java
        )

        Assertions.assertThat(responseEntity.statusCode == HttpStatus.BAD_REQUEST)
    }

    @Test
    internal fun `should be able to get the portfolio for a particular customer`() {
        val customerId = "1"
        restTemplate.postForEntity(url.plus("/load/daily/customers-strategies"), null, Void::class.java)
        val strategies = restTemplate.getForEntity(
            url.plus("/customer/").plus(customerId).plus("/strategies/"), Array<StrategyDto>::class.java
        ).body
        assert(strategies!!.isNotEmpty())

        val responseEntity = restTemplate.postForEntity(
            url.plus("/customer/").plus(customerId).plus("/strategy/").plus(strategies[0].strategyId),
            null,
            Void::class.java
        )

        assert(responseEntity.statusCode.is2xxSuccessful)
        assert(responseEntity.statusCode == HttpStatus.ACCEPTED)

        mockServerClient.`when`(HttpRequest.request().withMethod("GET").withPath("/customer/$customerId")).respond(
            HttpResponse.response().withStatusCode(200).withContentType(MediaType.APPLICATION_JSON).withBody(
                """{
                            "customerId": 1,
                            "stocks": 6700,
                            "bonds": 1200,
                            "cash": 400
                         }"""
            )
        )

        val tradeDto =
            restTemplate.getForEntity(url.plus("/customer/").plus(customerId).plus("/portfolio"), CustomerPortfolioDto::class.java).body!!

        Assertions.assertThat(tradeDto).isNotNull
        Assertions.assertThat(tradeDto.customerId == customerId)
    }


    @Test
    internal fun `should calculate trade for a particular customer`() {
        val customerId = "1"
        restTemplate.postForEntity(url.plus("/load/daily/customers-strategies"), null, Void::class.java)
        setupCustomerForTrade(customerId)
    }


    @Test
    internal fun `should execute trades for a customer`() {
        val customerId = "1"
        restTemplate.postForEntity(url.plus("/load/daily/customers-strategies"), null, Void::class.java)
        setupCustomerForTrade(customerId)

        mockServerClient.`when`(HttpRequest.request().withMethod("POST").withPath("/execute/")).respond(
            HttpResponse.response().withStatusCode(201).withContentType(MediaType.APPLICATION_JSON)
        )

        val executeResponse =
            restTemplate.postForEntity(url.plus("/customers/portfolio/trades"), CustomersRequest(customers = listOf(customerId)), Void::class.java)
        Assertions.assertThat(executeResponse.statusCode.is2xxSuccessful)
        Assertions.assertThat(executeResponse.statusCode == HttpStatus.CREATED)
    }


    @Test
    internal fun `should execute trades for multiple provided customers`() {
        val customerId = "1"
        val customerId2 = "1"

        restTemplate.postForEntity(url.plus("/load/daily/customers-strategies"), null, Void::class.java)

        setupCustomerForTrade(customerId)
        setupCustomerForTrade(customerId2)

        mockServerClient.`when`(HttpRequest.request().withMethod("POST").withPath("/execute/")).respond(
            HttpResponse.response().withStatusCode(201).withContentType(MediaType.APPLICATION_JSON)
        )

        val executeResponse =
            restTemplate.postForEntity(
                url.plus("/customers/portfolio/trades"),
                CustomersRequest(customers = listOf(customerId, customerId2)),
                Void::class.java
            )
        Assertions.assertThat(executeResponse.statusCode == HttpStatus.BAD_REQUEST)

    }

    private fun setupCustomerForTrade(customerId: String) {
        val strategies = restTemplate.getForEntity(
            url.plus("/customer/").plus(customerId).plus("/strategies/"), Array<StrategyDto>::class.java
        ).body
        Assertions.assertThat(strategies!!.isNotEmpty())

        val responseEntity = restTemplate.postForEntity(
            url.plus("/customer/").plus(customerId).plus("/strategy/").plus(strategies[0].strategyId),
            null,
            Void::class.java
        )

        Assertions.assertThat(responseEntity.statusCode.is2xxSuccessful)
        Assertions.assertThat(responseEntity.statusCode == HttpStatus.ACCEPTED)

        mockServerClient.`when`(HttpRequest.request().withMethod("GET").withPath("/customer/$customerId")).respond(
            HttpResponse.response().withStatusCode(200).withContentType(MediaType.APPLICATION_JSON).withBody(
                """{
                            "customerId": 1,
                            "stocks": 6700,
                            "bonds": 1200,
                            "cash": 400
                         }"""
            )
        )

        val tradeDto =
            restTemplate.getForEntity(url.plus("/customer/").plus(customerId).plus("/portfolio/trades"), CustomerTradeDto::class.java).body!!
        Assertions.assertThat(tradeDto.customerId == customerId)
    }
}