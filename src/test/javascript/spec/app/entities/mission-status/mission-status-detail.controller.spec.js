'use strict';

describe('Controller Tests', function() {

    describe('MissionStatus Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockMissionStatus, MockMission;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockMissionStatus = jasmine.createSpy('MockMissionStatus');
            MockMission = jasmine.createSpy('MockMission');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'MissionStatus': MockMissionStatus,
                'Mission': MockMission
            };
            createController = function() {
                $injector.get('$controller')("MissionStatusDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'jhipsterApp:missionStatusUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
