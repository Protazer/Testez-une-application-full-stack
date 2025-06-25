/// <reference types="Cypress" />

describe('Sessions page', () => {
  const ADMIN_DETAILS = {
    token: 'jwt',
    type: 'Bearer',
    id: 1,
    username: 'yoga@studio.com',
    firstName: 'Admin',
    lastName: 'Admin',
    admin: true,
  };

  const USER_DETAILS = {
    token: 'jwt',
    type: 'Bearer',
    id: 2,
    username: 'user@user.com',
    firstName: 'User',
    lastName: 'User',
    admin: false,
  };

  const TEST_SESSION = {
    id: 1,
    name: 'TEST session',
    date: '2025-06-11T13:27:22.000+00:00',
    teacher_id: 1,
    description: 'TEST description for the session',
    users: [2],
    createdAt: '2025-06-11T14:24:33',
    updatedAt: '2025-06-11T09:20:22',
  };

  const SESSIONS_LIST = [TEST_SESSION];

  const EDITED_TEST_SESSION = {
    ...TEST_SESSION,
    name: 'EDITED TEST session',
  };

  const TEACHERS_LIST = [
    {
      id: 1,
      lastName: 'SIMONETTO',
      firstName: 'Morgane',
      createdAt: '2025-06-12T15:33:42',
      updatedAt: '2025-06-12T15:33:42',
    },
    {
      id: 2,
      lastName: 'HUBERDEAU',
      firstName: 'Florian',
      createdAt: '2025-06-12T15:33:42',
      updatedAt: '2024-06-12T15:33:42',
    },
  ];

  beforeEach(() => {
    cy.intercept('GET', '/api/session', (req) => {
      req.reply(SESSIONS_LIST);
    });

    cy.intercept('POST', '/api/session', (req) => {
      SESSIONS_LIST.push(TEST_SESSION);
      req.reply(TEST_SESSION);
    });

    cy.intercept('GET', `/api/session/${TEST_SESSION.id}`, TEST_SESSION);

    cy.intercept(
      'POST',
      `/api/session/${TEST_SESSION.id}/participate/${USER_DETAILS.id}`,
      (req) => {
        req.reply({ statusCode: 200, body: {} });
      }
    );

    cy.intercept(
      'DELETE',
      `/api/session/${TEST_SESSION.id}/participate/${USER_DETAILS.id}`,
      (req) => {
        req.reply({ statusCode: 200, body: {} });
      }
    );

    cy.intercept('DELETE', `/api/session/${TEST_SESSION.id}`, (req) => {
      SESSIONS_LIST.splice(0, 1);
      req.reply(EDITED_TEST_SESSION);
    });

    cy.intercept('PUT', `/api/session/${TEST_SESSION.id}`, (req) => {
      SESSIONS_LIST.splice(0, 1, EDITED_TEST_SESSION);
      req.reply(EDITED_TEST_SESSION);
    });

    cy.intercept('GET', `/api/teacher`, TEACHERS_LIST);
    cy.intercept('GET', `/api/teacher/${TEACHERS_LIST[0].id}`, TEACHERS_LIST[0]);
  });

  describe('As an admin', () => {
    beforeEach(() => {
      cy.visit('/login');
      cy.intercept('POST', '/api/auth/login', ADMIN_DETAILS);

      cy.get('input[formControlName=email]').type('yoga@studio.com');
      cy.get('input[formControlName=password]').type('test!1234{enter}{enter}');

      cy.url().should('include', '/sessions');
    });

    it('Performs the following actions:', () => {
      cy.get('mat-card').should('have.length', 2);
      cy.get('mat-card-title').should('contain', TEST_SESSION.name);
      cy.get('button[mat-raised-button] span').contains('Create').click();
      cy.get('input[formControlName="name"]').type(TEST_SESSION.name);

      const formattedDate: string = TEST_SESSION.date.split('T')[0];
      cy.get('input[formControlName="date"]').type(formattedDate);
      cy.get('mat-select[formControlName="teacher_id"]').click();
      cy.get('mat-option').contains(TEACHERS_LIST[0].firstName).click();
      cy.get('textarea[formControlName="description"]').type(TEST_SESSION.description);
      cy.get('button[mat-raised-button]').contains('Save').click();

      cy.get('snack-bar-container').contains('Session created !').should('exist');
      cy.get('snack-bar-container button span').contains('Close').click();
      cy.get('mat-card').should('have.length', 3);

      cy.get('button[mat-raised-button] span').contains('Edit').click();
      cy.get('input[formControlName="name"]').clear().type('edited session');
      cy.get('button[mat-raised-button]').contains('Save').click();

      cy.get('snack-bar-container').contains('Session updated !').should('exist');
      cy.get('snack-bar-container button span').contains('Close').click();
      cy.get('mat-card-title').should('contain', EDITED_TEST_SESSION.name);

      cy.get('button').contains('Detail').click();
      cy.get('button').contains('Delete').click();

      cy.get('snack-bar-container').contains('Session deleted !').should('exist');
      cy.get('snack-bar-container button span').contains('Close').click();
      cy.get('mat-card').should('have.length', 2);
    });
  });

  describe('As a regular user', () => {
    beforeEach(() => {
      cy.visit('/login');
      cy.intercept('POST', '/api/auth/login', USER_DETAILS);

      cy.get('input[formControlName=email]').type('user@user.com');
      cy.get('input[formControlName=password]').type('test!1234{enter}{enter}');

      cy.url().should('include', '/sessions');
    });

    it('Performs the following actions:', () => {
      cy.get('button[mat-raised-button] span').contains('Edit').should('not.exist');
      cy.get('button[mat-raised-button] span').contains('Detail').click();
      cy.get('button').contains('Delete').should('not.exist');

      cy.get('button[mat-raised-button]').click();
      cy.get('button[mat-raised-button]').contains('Do not participate');

      cy.get('span').contains('attendees').then((span) => {
        const text: string = span.text();
        const attendeesCount: number = Number(text.match(/\d+/)![0]);
        expect(attendeesCount).equal(1);
      });

      cy.get('button[mat-raised-button]').click();
      cy.get('button[mat-raised-button]').contains('Do not participate');
    });
  });
});
