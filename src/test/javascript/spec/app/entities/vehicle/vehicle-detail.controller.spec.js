'use strict';

describe('Controller Tests', function() {

    describe('Vehicle Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockVehicle, MockUnit;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockVehicle = jasmine.createSpy('MockVehicle');
            MockUnit = jasmine.createSpy('MockUnit');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Vehicle': MockVehicle,
                'Unit': MockUnit
            };
            createController = function() {
                $injector.get('$controller')("VehicleDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'jhipsterApp:vehicleUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
