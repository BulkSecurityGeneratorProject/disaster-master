'use strict';

describe('Controller Tests', function() {

    describe('Fleet Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockFleet, MockUnit, MockService;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockFleet = jasmine.createSpy('MockFleet');
            MockUnit = jasmine.createSpy('MockUnit');
            MockService = jasmine.createSpy('MockService');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Fleet': MockFleet,
                'Unit': MockUnit,
                'Service': MockService
            };
            createController = function() {
                $injector.get('$controller')("FleetDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'jhipsterApp:fleetUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
