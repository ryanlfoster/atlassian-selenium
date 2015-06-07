/**
 * The API for creating and interacting with page objects for functional tests.
 *
 * <p>To get started, try:</p>
 * <ol>
 *  <li>Include the page objects for your product as a test-scoped dependency.</li>
 *  <li>Create a normal jUnit integration test that contains something like:
 *   <pre>
 *     ConfluenceTestedProduct product = TestedProductFactory.create(ConfluenceTestedProduct.class);
 *     DashboardPage dashboard = product.visit(ConfluenceLoginPage.class)
 *                                      .loginAsSysAdmin(DashboardPage.class);
 *   </pre>
 */
package com.atlassian.pageobjects;