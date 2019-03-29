	Feature: Create Order for existing customer

  Scenario: Create Order for existing customer
    When client requests POST on http://localhost.uri.ravisu.test:8003/shopping-cart/order-for-existing-customer with json data:
      """
      {   "custId": 1,    "itemSku": 1,    "ordQuantity": 1}
      """
    Then the response status should be 200
    And the response should not contain empty array
    
    Scenario: Create Order for new customer
    When client requests POST on http://localhost.uri.ravisu.test:8003/shopping-cart/order-for-new-customer with json data:
      """
          { "fname": "fistName",   "lname": "lastName",    "itemSku": 1,    "ordQuantity": 10 }
      """
    Then the response status should be 200    
    And the response should not contain empty array
