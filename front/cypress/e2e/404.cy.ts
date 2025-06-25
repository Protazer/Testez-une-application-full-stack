///<reference types="Cypress"/>

describe('404 page', () => {
  it("should show 404 page when the page doesn't exist", () => {
    cy.visit('/not-found-url');
    cy.get('h1').should('contain','Page not found !');
  })
})
