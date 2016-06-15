'use strict';

describe('Controller Tests', function() {

    describe('Service Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockService, MockFleet;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockService = jasmine.createSpy('MockService');
            MockFleet = jasmine.createSpy('MockFleet');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Service': MockService,
                'Fleet': MockFleet
            };
            createController = function() {
                $injector.get('$controller')("ServiceDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'jhipsterApp:serviceUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
